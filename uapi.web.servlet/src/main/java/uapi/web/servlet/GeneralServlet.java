package uapi.web.servlet;

import uapi.behavior.*;
import uapi.config.annotation.Config;
import uapi.event.IEventBus;
import uapi.log.ILogger;
import uapi.service.annotation.Inject;
import uapi.service.annotation.OnActivate;
import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;
import uapi.web.http.HttpRequestEvent;
import uapi.web.http.HttpResponseEvent;
import uapi.web.servlet.internal.ServletRequest;
import uapi.web.servlet.internal.ServletResponse;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * A responsible to handle servlet request and response
 */
@Service
@Tag("ServletApp")
public class GeneralServlet extends HttpServlet {

    private static final String RESPONSIBLE_NAME    = "Servlet";

    private static final String BEHAVIOR_RESPONSE   = "Response";

    private final IAnonymousAction<Exception, BehaviorEvent> DEFAULT_FAILURE_ACTION = (ex, ctx) -> {
        this._logger.error(ex, "Fail to process behavior - {}", ctx.behaviorName());
        return null;
    };

    @Config(path="web.servlet.url-mappings")
    protected List<String> _mappedUrls;

    @Inject
    protected ILogger _logger;

    @Inject
    protected IEventBus _eventBus;

    @Inject
    protected IResponsibleRegistry _responsibleReg;

    public List<String> mappedUrls() {
        return this._mappedUrls;
    }

    @OnActivate
    public void activate() {
        IResponsible responsible = this._responsibleReg.register(RESPONSIBLE_NAME);
        responsible.newBehavior(BEHAVIOR_RESPONSE, HttpResponseEvent.class, HttpResponseEvent.TOPIC)
                .then((input, execCtx) -> {
                    try {
                        ((HttpResponseEvent) input).response().complete();
                    } catch (IOException ex) {
                        this._logger.error(ex);
                    }
                    return null;
                })
                .onFailure(DEFAULT_FAILURE_ACTION)
                .build();
    }

    @Override
    public void doGet(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException {
        AsyncContext ctx = request.startAsync();
        HttpRequestEvent event = new HttpRequestEvent(
                RESPONSIBLE_NAME,
                new ServletRequest(ctx),
                new ServletResponse(ctx));
        this._eventBus.fire(event);
    }
}
