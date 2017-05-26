package uapi.web.servlet.internal;

import uapi.common.StringHelper;
import uapi.web.http.IHttpResponse;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet response implementation for IHttpResponse
 */
public class ServletResponse implements IHttpResponse {

    private final AsyncContext _asyncCtx;

    public ServletResponse(final AsyncContext asyncContext) {
        this._asyncCtx = asyncContext;
    }

    @Override
    public void output(String message) throws IOException {
        output(message, new String[0]);
    }

    @Override
    public void output(
            final String messageTemplate,
            final String... args
    ) throws IOException {
        PrintWriter out = this._asyncCtx.getResponse().getWriter();
        String msg = StringHelper.makeString(messageTemplate, args);
        out.print(msg);
    }

    @Override
    public void complete() throws IOException {
        this._asyncCtx.getResponse().getWriter().flush();
        this._asyncCtx.complete();
    }
}
