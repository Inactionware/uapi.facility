package uapi.web.servlet.internal;

import uapi.common.StringHelper;
import uapi.rx.Looper;
import uapi.web.http.IHttpRequest;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet request implementation of IHttpRequest
 */
public class ServletRequest implements IHttpRequest {

    private final AsyncContext _asyncCtx;
    private final HttpServletRequest _request;
    private final HttpServletResponse _response;

    public ServletRequest(final AsyncContext asyncContext) {
        this._asyncCtx = asyncContext;
        this._request = (HttpServletRequest) this._asyncCtx.getRequest();
        this._response = (HttpServletResponse) this._asyncCtx.getResponse();
    }

    @Override
    public String url() {
        return this._request.getRequestURL().toString();
    }

    @Override
    public Map<String, String[]> parameters() {
        return this._request.getParameterMap();
    }

    @Override
    public String parameter(String name) {
        return this._request.getParameter(name);
    }

    @Override
    public String header(String name) {
        return this._request.getHeader(name);
    }
}
