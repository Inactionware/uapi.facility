package uapi.example.web;

import uapi.behavior.ActionIdentify;
import uapi.behavior.IResponsible;
import uapi.behavior.IResponsibleRegistry;
import uapi.service.IRegistry;
import uapi.service.annotation.Inject;
import uapi.service.annotation.OnActivate;
import uapi.service.annotation.Service;
import uapi.service.annotation.Tag;
import uapi.web.http.ExtractRequest;
import uapi.web.http.HttpRequestEvent;
import uapi.web.http.HttpResponseEvent;

/**
 * Created by xquan on 5/27/2017.
 */
@Service(autoActive = true)
@Tag("ServletApp")
public class HelloUser {

    private static final String RESP_NAME   = HelloUser.class.getName();

    @Inject
    protected IResponsibleRegistry _respReg;

    @Inject
    IRegistry _reg;

    @OnActivate
    public void activate() {
        IResponsible resp = this._respReg.register(RESP_NAME);
        resp.newBehavior("Say Hello User", HttpRequestEvent.class, HttpRequestEvent.TOPIC)
                .then(ActionIdentify.toActionId(ExtractRequest.class))
                .call((input, execCtx) -> {
                    User user = (User) input;
                    HttpRequestEvent event = execCtx.originalEvent();
                    event.response().output("Hello Web event by user " + user.name + "!");
                })
                .onSuccess((input, execCtx) -> {
                    HttpRequestEvent event = execCtx.originalEvent();
                    return new HttpResponseEvent(RESP_NAME, event.request(), event.response());
                })
                .build();
    }
}
