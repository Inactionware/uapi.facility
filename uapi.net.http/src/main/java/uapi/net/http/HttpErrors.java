package uapi.net.http;

import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpErrors extends FileBasedExceptionErrors<HttpException> {

    public static final int CATEGORY    = 0x0202;

    public static final int UNSUPPORTED_HTTP_VERSION    = 1;
    public static final int UNSUPPORTED_HTTP_METHOD     = 2;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(UNSUPPORTED_HTTP_VERSION, UnsupportedHttpVersion.KEY);
        keyCodeMapping.put(UNSUPPORTED_HTTP_METHOD, UnsupportedHttpMethod.KEY);
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
}
