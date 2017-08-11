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
    public static final int MORE_THEN_MAX_VALUE             = 5;
    public static final int LESS_THEN_MIN_VALUE             = 6;
    public static final int SIZE_OVER_LIMITATION            = 7;
    public static final int UNMATCHED_VALUE_BY_REGEXP       = 8;

    private static final Map<Integer, String> keyCodeMapping;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(INVALID_PARAM_TRUE_FALSE, InvalidParamTrueFalse.KEY);
        keyCodeMapping.put(INVALID_PARAM_YES_NO, InvalidParamYesNo.KEY);
        keyCodeMapping.put(INVALID_PARAM_ON_OFF, InvalidParamOnOff.KEY);
        keyCodeMapping.put(PARAM_IS_REQUIRED, ParamIsRequired.KEY);
        keyCodeMapping.put(MORE_THEN_MAX_VALUE, MoreThanMaxValue.KEY);
        keyCodeMapping.put(LESS_THEN_MIN_VALUE, LessThanMinValue.KEY);
        keyCodeMapping.put(SIZE_OVER_LIMITATION, SizeOverLimitation.KEY);
        keyCodeMapping.put(UNMATCHED_VALUE_BY_REGEXP, UnmatchedValueByRegexp.KEY);
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

    /**
     * Error string template:
     *      The parameter [{} -> {}] is more than specified value - {}, number type - {}
     */
    public static final class MoreThanMaxValue extends IndexedParameters<MoreThanMaxValue> {

        public static final String KEY  = "MoreThanMaxValue";

        private String _name;
        private String _value;
        private Number _limitation;
        private NumberValidator.NumberType _type;

        public MoreThanMaxValue name(String name) {
            this._name = name;
            return this;
        }

        public MoreThanMaxValue value(String value) {
            this._value = value;
            return this;
        }

        public MoreThanMaxValue limitation(Number limitation) {
            this._limitation = limitation;
            return this;
        }

        public MoreThanMaxValue type(NumberValidator.NumberType type) {
            this._type = type;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name, this._value, this._limitation, this._type };
        }
    }

    /**
     * Error string template:
     *      The parameter [{} -> {}] is less than specified value - {}, number type - {}
     */
    public static final class LessThanMinValue extends IndexedParameters<LessThanMinValue> {

        public static final String KEY  = "LessThanMinValue";

        private String _name;
        private String _value;
        private Number _limitation;
        private NumberValidator.NumberType _type;

        public LessThanMinValue name(String name) {
            this._name = name;
            return this;
        }

        public LessThanMinValue value(String value) {
            this._value = value;
            return this;
        }

        public LessThanMinValue limitation(Number limitation) {
            this._limitation = limitation;
            return this;
        }

        public LessThanMinValue type(NumberValidator.NumberType type) {
            this._type = type;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name, this._value, this._limitation, this._type };
        }
    }

    /**
     * Error string template:
     *      The parameter [{} -> {}] size is over limitation - min: {}, max: {}
     */
    public static final class SizeOverLimitation extends IndexedParameters<SizeOverLimitation> {

        public static final String KEY  = "SizeOverLimitation";

        private String _name;
        private String _value;
        private int _min;
        private int _max;

        public SizeOverLimitation name(String name) {
            this._name = name;
            return this;
        }

        public SizeOverLimitation value(String value) {
            this._value = value;
            return this;
        }

        public SizeOverLimitation min(int min) {
            this._min = min;
            return this;
        }

        public SizeOverLimitation max(int max) {
            this._max = max;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name, this._value, this._min, this._max };
        }
    }

    /**
     * Error string template:
     *      The parameter [{} -> {}] does not match the regexp - {}
     */
    public static final class UnmatchedValueByRegexp extends IndexedParameters<UnmatchedValueByRegexp> {

        public static final String KEY  = "UnmatchedValueByRegexp";

        private String _name;
        private String _value;
        private String _regexp;

        public UnmatchedValueByRegexp name(String name) {
            this._name = name;
            return this;
        }

        public UnmatchedValueByRegexp value(String value) {
            this._value = value;
            return this;
        }

        public UnmatchedValueByRegexp regexp(String regexp) {
            this._regexp = regexp;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._name, this._value, this._regexp };
        }
    }
}
