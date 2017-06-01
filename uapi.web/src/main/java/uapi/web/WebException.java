package uapi.web;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ParameterizedException;

/**
 * The root exception for web framework
 */
public class WebException extends ParameterizedException {

    public static WebExceptionBuilder builder() {
        return new WebExceptionBuilder();
    }

    protected WebException(ExceptionBuilder builder) {
        super(builder);
    }

    public static final class WebExceptionBuilder
            extends ExceptionBuilder<WebException, WebExceptionBuilder> {

        private WebExceptionBuilder() {
            super(WebErrors.CATEGORY, new WebErrors());
        }

        @Override
        protected WebException createInstance() {
            return new WebException(this);
        }
    }
}
