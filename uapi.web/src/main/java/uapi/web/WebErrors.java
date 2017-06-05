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
    public static final int PARAM_IS_REQUIRED               = 4;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(INVALID_PARAM_TRUE_FALSE, InvalidParamTrueFalse.KEY);
        keyCodeMapping.put(INVALID_PARAM_YES_NO, InvalidParamYesNo.KEY);
        keyCodeMapping.put(INVALID_PARAM_ON_OFF, InvalidParamOnOff.KEY);
        keyCodeMapping.put(PARAM_IS_REQUIRED, ParamIsRequired.KEY);
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

        private String _name;
        private String _value;

        public InvalidParamTrueFalse name(String name) {
            this._name = name;
            return this;
        }

        public InvalidParamTrueFalse value(String value) {
            this._value = value;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name, this._value };
        }
    }

    /**
     * Error string template:
     *      Only YES and NO is allowed on the parameter - {}
     */
    public static final class InvalidParamYesNo extends IndexedParameters<InvalidParamYesNo> {

        public static final String KEY  = "InvalidParamYesNo";

        private String _name;
        private String _value;

        public InvalidParamYesNo name(String name) {
            this._name = name;
            return this;
        }

        public InvalidParamYesNo value(String value) {
            this._value = value;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name, this._value };
        }
    }

    /**
     * Error string template:
     *      Only ON and OFF is allowed on the parameter - {}
     */
    public static final class InvalidParamOnOff extends IndexedParameters<InvalidParamOnOff> {

        public static final String KEY  = "InvalidParamOnOff";

        private String _name;
        private String _value;

        public InvalidParamOnOff name(String name) {
            this._name = name;
            return this;
        }

        public InvalidParamOnOff value(String value) {
            this._value = value;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name, this._value };
        }
    }

    /**
     * Error string template:
     *      The parameter is required - {}
     */
    public static final class ParamIsRequired extends IndexedParameters<ParamIsRequired> {

        public static final String KEY  = "ParamIsRequired";

        private String _name;

        public ParamIsRequired name(String name) {
            this._name = name;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name };
        }
    }
}
