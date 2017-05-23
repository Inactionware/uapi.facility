package uapi.servlet;

import uapi.Tags;
import uapi.app.AppErrors;
import uapi.app.AppException;
import uapi.app.internal.AppServiceLoader;
import uapi.app.internal.SystemShuttingDownEvent;
import uapi.app.internal.SystemStartingUpEvent;
import uapi.common.CollectionHelper;
import uapi.event.IEventBus;
import uapi.rx.Looper;
import uapi.service.IRegistry;
import uapi.service.IService;
import uapi.service.ITagged;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xquan on 5/23/2017.
 */
public class ServletBootstrap implements Filter {

    private static final String[] basicSvcTags = new String[] {
            Tags.REGISTRY, Tags.CONFIG, Tags.LOG, Tags.EVENT, Tags.BEHAVIOR,
            Tags.PROFILE, Tags.APPLICATION
    };

    static AppServiceLoader appSvcLoader = new AppServiceLoader();

    private List<IService> _basicSvcs;
    private List<IService> _otherSvcs;
    private IRegistry _svcRegistry;

    @Override
    public void init(
            final FilterConfig filterConfig
    ) throws ServletException {
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

        // TODO: read config from filterConfig

        // All base service must be activated
        Looper.on(basicSvcTags).foreach(this._svcRegistry::activateTaggedService);

        // Send system starting up event
        SystemStartingUpEvent sysLaunchedEvent = new SystemStartingUpEvent(startTime, this._otherSvcs);
        IEventBus eventBus = this._svcRegistry.findService(IEventBus.class);
//        eventBus.register(new ExitSystemRequestHandler());
        eventBus.fire(sysLaunchedEvent);
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {

    }

    @Override
    public void destroy() {
        // Send system shutting down event
        SystemShuttingDownEvent shuttingDownEvent = new SystemShuttingDownEvent(this._otherSvcs, null);
        IEventBus eventBus = this._svcRegistry.findService(IEventBus.class);
        eventBus.fire(shuttingDownEvent, true);
    }
}
