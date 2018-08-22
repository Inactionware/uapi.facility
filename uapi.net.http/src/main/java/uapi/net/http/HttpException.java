package uapi.net.http;

import uapi.common.ArgumentChecker;
import uapi.exception.ExceptionBuilder;
import uapi.exception.ExceptionErrors;
import uapi.exception.ParameterizedException;

public class HttpException extends ParameterizedException {

    public static HttpExceptionBuilder builder() {
        return new HttpExceptionBuilder(HttpErrors.CATEGORY, new HttpErrors());
    }

    private HttpException(final HttpExceptionBuilder builder) {
        super(builder);
    }

    public HttpStatus status() {
        return ((HttpExceptionBuilder) this._builder)._status;
    }

    public static final class HttpExceptionBuilder extends ExceptionBuilder<HttpException, HttpExceptionBuilder> {

        private HttpStatus _status = HttpStatus.INTERNAL_SERVER_ERROR;

        private HttpExceptionBuilder(
                final int category,
                final ExceptionErrors<HttpException> errors
        ) {
            super(category, errors);
        }

        @Override
        protected HttpException createInstance() {
            return new HttpException(this);
        }

        public HttpExceptionBuilder status(
                final HttpStatus status
        ) {
            ArgumentChecker.required(status, "status");

            this._status = status;
            return this;
        }
    }
}
