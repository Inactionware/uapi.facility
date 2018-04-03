package uapi.net.http;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ExceptionErrors;
import uapi.exception.ParameterizedException;

public class HttpException extends ParameterizedException {

    public static HttpExceptionBuilder builder() {
        return new HttpExceptionBuilder(HttpErrors.CATEGORY, new HttpErrors());
    }

    protected HttpException(final HttpExceptionBuilder builder) {
        super(builder);
    }

    public static final class HttpExceptionBuilder extends ExceptionBuilder<HttpException, HttpExceptionBuilder> {

        public HttpExceptionBuilder(
                final int category,
                final ExceptionErrors<HttpException> errors
        ) {
            super(category, errors);
        }

        @Override
        protected HttpException createInstance() {
            return new HttpException(this);
        }
    }
}
