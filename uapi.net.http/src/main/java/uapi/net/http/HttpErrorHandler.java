package uapi.net.http;

import uapi.GeneralException;
import uapi.common.ArgumentChecker;
import uapi.net.IErrorHandler;
import uapi.net.IResponse;

public class HttpErrorHandler implements IErrorHandler {

    @Override
    public void handle(Exception exception, IResponse response) {
        ArgumentChecker.required(exception, "exception");
        ArgumentChecker.required(response, "response");
        if (! (response instanceof IHttpResponse)) {
            throw new GeneralException("Can't handle error for response - {}", response.getClass().getCanonicalName());
        }

        IHttpResponse httpResponse = (IHttpResponse) response;
        httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        httpResponse.write(exception.getMessage());
        httpResponse.flush();
    }
}
