package uapi.app.web.servlet;

import uapi.GeneralException;
import uapi.app.AppStartupEvent;
import uapi.app.SystemBootstrap;
import uapi.app.internal.SystemShuttingDownEvent;
import uapi.app.web.servlet.internal.ContextConfigProvider;
import uapi.behavior.IResponsible;
import uapi.behavior.IResponsibleRegistry;
import uapi.event.IEventBus;
import uapi.rx.Looper;
import uapi.service.IRegistry;
import uapi.service.IService;
import uapi.web.servlet.GeneralServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import java.util.List;

/**
 * The bootstrap launch UAPI framework from servlet environment
 */
public class Bootstrap extends SystemBootstrap implements ServletContextListener {

    private ServletContext _servletCtx;
    private IRegistry _registry;
    private List<IService> _appServices;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this._servletCtx = sce.getServletContext();
        boot();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Send system shutting down event
        SystemShuttingDownEvent shuttingDownEvent = new SystemShuttingDownEvent(this._appServices, null);
        IEventBus eventBus = this._registry.findService(IEventBus.class);
        eventBus.fire(shuttingDownEvent, true);
    }

    @Override
    protected void loadConfig(final IRegistry registry) {
        ContextConfigProvider cfgProvider = registry.findService(ContextConfigProvider.class);
        if (cfgProvider == null) {
            throw new GeneralException("The servlet config provider can't be found in registry");
        }
        cfgProvider.parse(this._servletCtx);
    }

    @Override
    protected void beforeSystemLaunching(
            final IRegistry registry,
            final List<IService> appServices
    ) {
        this._registry = registry;
        this._appServices = appServices;

        IResponsibleRegistry respReg = this._registry.findService(IResponsibleRegistry.class);
        IResponsible resp = respReg.register("ServletRegister");
        resp.newBehavior("RegisterServlet", AppStartupEvent.class, AppStartupEvent.TOPIC)
                .then((input, execCtx) -> {
                    GeneralServlet servlet = this._registry.findService(GeneralServlet.class);
                    ServletRegistration.Dynamic servletCfg =
                            this._servletCtx.addServlet("GeneralServlet", servlet);
                    Looper.on(servlet.mappedUrls()).foreach(servletCfg::addMapping);
                    servletCfg.setAsyncSupported(true);
                    return null;
                })
                .build();
    }

    @Override
    protected void afterSystemLaunching(
            final IRegistry iRegistry,
            final List<IService> appServices
    ) {
        // do nothing
    }
}
