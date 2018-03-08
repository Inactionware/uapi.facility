package uapi.net;

import uapi.exception.ExceptionBuilder;
import uapi.exception.ParameterizedException;

/**
 * The root exception of net framework
 */
public class NetException extends ParameterizedException {

    protected NetException(ExceptionBuilder builder) {
        super(builder);
    }

    public static final class NetExceptionBuilder
            extends ExceptionBuilder<NetException, NetExceptionBuilder> {

        private NetExceptionBuilder() {
            super(NetErrors.CATEGORY, new NetErrors());
        }

        @Override
        protected NetException createInstance() {
            return new NetException(this);
        }
    }
}
