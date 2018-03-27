package uapi.net.http.netty.internal;

import uapi.net.http.ContentType;
import uapi.net.http.HttpMethod;
import uapi.net.http.HttpVersion;
import uapi.net.http.IHttpRequest;

import java.util.List;
import java.util.Map;

public class NettyHttpRequest implements IHttpRequest {

    @Override
    public HttpVersion version() {
        return null;
    }

    @Override
    public HttpMethod method() {
        return null;
    }

    @Override
    public ContentType conentType() {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    @Override
    public Map<String, List<String>> params() {
        return null;
    }
}
