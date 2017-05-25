package uapi.app.web.servlet;

import uapi.GeneralException;
import uapi.Tags;
import uapi.app.AppErrors;
import uapi.app.AppException;
import uapi.app.internal.AppServiceLoader;
import uapi.app.internal.SystemShuttingDownEvent;
import uapi.app.internal.SystemStartingUpEvent;
import uapi.app.web.servlet.internal.ContextConfigProvider;
import uapi.common.CollectionHelper;
import uapi.event.IEventBus;
import uapi.rx.Looper;
import uapi.service.IRegistry;
import uapi.service.IService;
import uapi.service.ITagged;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The bootstrap launch UAPI framework from servlet environment
 */
public class Bootstrap implements ServletContextListener {

    private static final String[] basicSvcTags = new String[] {
            Tags.REGISTRY, Tags.CONFIG, Tags.LOG, Tags.EVENT, Tags.BEHAVIOR,
            Tags.PROFILE, Tags.APPLICATION
    };

    static AppServiceLoader appSvcLoader = new AppServiceLoader();

    private List<IService> _basicSvcs;
    private List<IService> _otherSvcs;
    private IRegistry _svcRegistry;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        long startTime = System.currentTimeMillis();

        Iterable<IService> svcLoaders = appSvcLoader.loadServices();
        final List<IRegistry> svcRegistries = new ArrayList<>();
        this._basicSvcs = new ArrayList<>();
        this._otherSvcs = new ArrayList<>();
        Looper.on(svcLoaders)
                .foreach(svc -> {
                    if (svc instanceof IRegistry) {
                        svcRegistries.add((IRegistry) svc);
                    }
                    if (svc instanceof ITagged) {
                        ITagged taggedSvc = (ITagged) svc;
                        String[] tags = taggedSvc.getTags();
                        if (CollectionHelper.contains(tags, basicSvcTags) != null) {
                            this._basicSvcs.add(svc);
                        } else {
                            this._otherSvcs.add(svc);
                        }
                    } else {
                        this._otherSvcs.add(svc);
                    }
                });

        if (svcRegistries.size() == 0) {
            throw AppException.builder()
                    .errorCode(AppErrors.REGISTRY_IS_REQUIRED)
                    .build();
        }
        if (svcRegistries.size() > 1) {
            throw AppException.builder()
                    .errorCode(AppErrors.MORE_REGISTRY)
                    .variables(new AppErrors.MoreRegistry()
                            .registries(svcRegistries))
                    .build();
        }

        this._svcRegistry = svcRegistries.get(0);
        // Register basic service first
        this._svcRegistry.register(this._basicSvcs.toArray(new IService[this._basicSvcs.size()]));
        String svcRegType = this._svcRegistry.getClass().getCanonicalName();
        this._svcRegistry = this._svcRegistry.findService(IRegistry.class);
        if (this._svcRegistry == null) {
            throw AppException.builder()
                    .errorCode(AppErrors.REGISTRY_IS_UNSATISFIED)
                    .variables(new AppErrors.RepositoryIsUnsatisfied()
                            .serviceRegistryType(svcRegType))
                    .build();
        }

        // parse context configuration
        ContextConfigProvider cfgProvider = this._svcRegistry.findService(ContextConfigProvider.class);
        if (cfgProvider == null) {
            throw new GeneralException("The servlet config provider can't be found in registry");
        }
        cfgProvider.parse(sce.getServletContext());

        // All base service must be activated
        Looper.on(basicSvcTags).foreach(this._svcRegistry::activateTaggedService);

        // Send system starting up event
        SystemStartingUpEvent sysLaunchedEvent = new SystemStartingUpEvent(startTime, this._otherSvcs);
        IEventBus eventBus = this._svcRegistry.findService(IEventBus.class);
        eventBus.fire(sysLaunchedEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Send system shutting down event
        SystemShuttingDownEvent shuttingDownEvent = new SystemShuttingDownEvent(this._otherSvcs, null);
        IEventBus eventBus = this._svcRegistry.findService(IEventBus.class);
        eventBus.fire(shuttingDownEvent, true);
    }
}
