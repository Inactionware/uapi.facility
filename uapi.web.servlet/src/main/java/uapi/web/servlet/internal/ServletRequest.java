package uapi.web.servlet.internal;

import uapi.web.http.IHttpRequest;

import javax.servlet.AsyncContext;

/**
 * Servlet request implementation of IHttpRequest
 */
public class ServletRequest implements IHttpRequest {

    private final AsyncContext _asyncCtx;

    public ServletRequest(final AsyncContext asyncContext) {
        this._asyncCtx = asyncContext;
    }
}
