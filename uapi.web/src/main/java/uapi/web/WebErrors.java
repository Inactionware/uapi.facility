package uapi.web;

import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Error codes for Web
 */
public class WebErrors extends FileBasedExceptionErrors<WebException> {

    public static final int CATEGORY    = 0x0200;

    public static final int INVALID_PARAM_TRUE_FALSE        = 1;
    public static final int INVALID_PARAM_YES_NO            = 2;
    public static final int INVALID_PARAM_ON_OFF            = 3;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(INVALID_PARAM_TRUE_FALSE, InvalidParamTrueFalse.KEY);
        keyCodeMapping.put(INVALID_PARAM_YES_NO, InvalidParamYesNo.KEY);
        keyCodeMapping.put(INVALID_PARAM_ON_OFF, InvalidParamOnOff.KEY);
    }

    @Override
    protected String getFile(WebException ex) {
        if (ex.category() == CATEGORY) {
            return "/webErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(WebException ex) {
        return keyCodeMapping.get(ex.errorCode());
    }

    /**
     * Error string template:
     *      Only TRUE and FALSE is allowed on the parameter - {}
     */
    public static final class InvalidParamTrueFalse extends IndexedParameters<InvalidParamTrueFalse> {

        public static final String KEY  = "InvalidParamTrueFalse";

        private String _value;

        public InvalidParamTrueFalse value(String value) {
            this._value = value;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._value };
        }
    }

    /**
     * Error string template:
     *      Only YES and NO is allowed on the parameter - {}
     */
    public static final class InvalidParamYesNo extends IndexedParameters<InvalidParamYesNo> {

        public static final String KEY  = "InvalidParamYesNo";

        private String _value;

        public InvalidParamYesNo value(String value) {
            this._value = value;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._value };
        }
    }

    /**
     * Error string template:
     *      Only ON and OFF is allowed on the parameter - {}
     */
    public static final class InvalidParamOnOff extends IndexedParameters<InvalidParamOnOff> {

        public static final String KEY  = "InvalidParamOnOff";

        private String _value;

        public InvalidParamOnOff value(String value) {
            this._value = value;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._value };
        }
    }
}
