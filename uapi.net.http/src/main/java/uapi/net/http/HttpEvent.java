package uapi.net.http;

import uapi.common.ArgumentChecker;
import uapi.net.INetEvent;

public class HttpEvent implements INetEvent {

    public static final String TOPIC    = "HttpRequest";

    private final String _type;
    private final IHttpRequest _request;
    private final IHttpResponse _response;

    public HttpEvent(
            final String type,
            final IHttpRequest request,
            final IHttpResponse response
    ) {
        ArgumentChecker.required(type, "type");
        ArgumentChecker.required(request, "request");
        ArgumentChecker.required(response, "response");

        this._type = type;
        this._request = request;
        this._response = response;
    }

    @Override
    public String type() {
        return null;
    }

    @Override
    public String topic() {
        return TOPIC;
    }

    @Override
    public IHttpRequest request() {
        return this._request;
    }

    @Override
    public IHttpResponse response() {
        return this._response;
    }
}
