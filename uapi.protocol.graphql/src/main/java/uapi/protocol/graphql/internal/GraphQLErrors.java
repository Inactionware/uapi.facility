package uapi.protocol.graphql.internal;

import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;
import uapi.net.http.HttpMethod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GraphQLErrors extends FileBasedExceptionErrors<GraphQLException> {

    public static final int CATEGORY    = 0x0211;

    public static final int ONE_QUERY_PARAM_FOR_GET     = 1;
    public static final int UNSUPPORTED_METHOD          = 2;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(ONE_QUERY_PARAM_FOR_GET, OneQueryParamForGet.KEY);
        keyCodeMapping.put(UNSUPPORTED_METHOD, UnsupportedMethod.KEY);
    }

    @Override
    protected String getFile(GraphQLException exception) {
        if (exception.category() == CATEGORY) {
            return "/graphqlErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(GraphQLException exception) {
        return keyCodeMapping.get(exception.errorCode());
    }

    /**
     * Error string template:
     *      GraphQL only allows one query parameter for HTTP GET request
     */
    public static final class OneQueryParamForGet extends IndexedParameters<OneQueryParamForGet> {

        private static final String KEY = "OneQueryParamForGet";
    }

    /**
     * Error string template:
     *      GraphQL does not support http method - {}
     */
    public static final class UnsupportedMethod extends IndexedParameters<UnsupportedMethod> {

        private static final String KEY = "UnsupportedMethod";

        private HttpMethod _method;

        public void method(HttpMethod method) {
            this._method = method;
        }

        public Object[] get() {
            return new Object[] { this._method };
        }
    }
}
