package uapi.example.web;

import uapi.behavior.IResponsible;
import uapi.behavior.IResponsibleRegistry;
import uapi.service.annotation.Inject;
import uapi.service.annotation.OnActivate;
import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;
import uapi.web.http.HttpRequestEvent;
import uapi.web.http.HttpResponseEvent;

/**
 * Created by xquan on 5/27/2017.
 */
@Service(autoActive = true)
@Tag("ServletApp")
public class WebHelloWorld {

    private static final String RESP_NAME   = WebHelloWorld.class.getName();

    @Inject
    protected IResponsibleRegistry _respReg;

    @OnActivate
    public void activate() {
        IResponsible resp = this._respReg.register(RESP_NAME);
        resp.newBehavior("Say Hello", HttpRequestEvent.class, HttpRequestEvent.TOPIC)
                .then((input, execCtx) -> {
                    HttpRequestEvent event = (HttpRequestEvent) input;
                    event.response().output("Hello from Web event!");
                    return event;
                })
                .onSuccess((input, execCtx) -> {
                    HttpRequestEvent event = (HttpRequestEvent) input;
                    return new HttpResponseEvent(RESP_NAME, event.request(), event.response());
                })
                .build();
    }
}
