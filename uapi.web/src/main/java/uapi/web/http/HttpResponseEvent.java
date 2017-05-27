package uapi.web.http;

import uapi.behavior.BehaviorEvent;

/**
 * Indicate a http request
 */
public class HttpResponseEvent extends BehaviorEvent {

    public static final String TOPIC    = "HttpResponseEvent";

    private static final String KEY_REQ = "Request";
    private static final String KEY_RES = "Response";

    public HttpResponseEvent(
            final String sourceName,
            final IHttpRequest request,
            final IHttpResponse response) {
        super(TOPIC, sourceName);
        set(KEY_REQ, request);
        set(KEY_RES, response);
    }

    public IHttpRequest request() {
        return (IHttpRequest) get(KEY_REQ);
    }

    public IHttpResponse response() {
        return (IHttpResponse) get(KEY_RES);
    }
}
