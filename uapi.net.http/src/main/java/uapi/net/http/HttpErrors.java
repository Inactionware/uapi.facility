package uapi.net.http;

import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpErrors extends FileBasedExceptionErrors<HttpException> {

    public static final int CATEGORY    = 0x0202;

    public static final int UNSUPPORTED_HTTP_VERSION        = 1;
    public static final int UNSUPPORTED_HTTP_METHOD         = 2;
    public static final int ILLEGAL_LISTENER_STATE_SWITCH   = 3;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(UNSUPPORTED_HTTP_VERSION, UnsupportedHttpVersion.KEY);
        keyCodeMapping.put(UNSUPPORTED_HTTP_METHOD, UnsupportedHttpMethod.KEY);
        keyCodeMapping.put(ILLEGAL_LISTENER_STATE_SWITCH, IllegalListenerStateSwitch.KEY);
    }

    @Override
    protected String getFile(HttpException ex) {
        if (ex.category() == CATEGORY) {
            return "/httpErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(HttpException ex) {
        return keyCodeMapping.get(ex.errorCode());
    }

    protected static abstract class GeneralHttpError<T extends IndexedParameters> extends IndexedParameters<T> {

        private HttpStatus _status = HttpStatus.INTERNAL_SERVER_ERROR;

        public T status(
                final HttpStatus status
        ) {
            this._status = status;
            return (T) this;
        }

        public HttpStatus status() {
            return this._status;
        }
    }

    /**
     * Error string template:
     *      The HTTP version is not supported - {}
     */
    public static final class UnsupportedHttpVersion extends GeneralHttpError<UnsupportedHttpVersion> {

        private static final String KEY = "UnsupportedHttpVersion";

        private String _verTxt;

        public UnsupportedHttpVersion versionText(
                final String text
        ) {
            this._verTxt = text;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._verTxt };
        }
    }

    /**
     * Error string template:
     *      The HTTP method is not supported - {}
     */
    public static final class UnsupportedHttpMethod extends GeneralHttpError<UnsupportedHttpMethod> {

        private static final String KEY = "UnsupportedHttpMethod";

        private String _method;

        public UnsupportedHttpMethod method(
                final String method
        ) {
            this._method = method;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._method };
        }
    }

    /**
     * Error string template:
     *      It is not allowed do operation {} on state {} for listen on {}:{}
     */
    public static final class IllegalListenerStateSwitch extends GeneralHttpError<IllegalListenerStateSwitch> {

        private static final String KEY = "IllegalListenerStateSwitch";

        private String _fromState;
        private String _operation;
        private String _host;
        private int _port;

        public IllegalListenerStateSwitch fromState(
                final String state
        ) {
            this._fromState = state;
            return this;
        }

        public IllegalListenerStateSwitch operation(
                final String operation
        ) {
            this._operation = operation;
            return this;
        }

        public IllegalListenerStateSwitch host(
                final String host
        ) {
            this._host = host;
            return this;
        }

        public IllegalListenerStateSwitch port(
                final int port
        ) {
            this._port = port;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._operation, this._fromState, this._host, this._port };
        }
    }
}
