package uapi.web;

/**
 * The validator verify specific input to ensure it does not less than specified value
 */
public class MinValidator extends NumberValidator {

    public MinValidator(short shortValue) {
        super(shortValue);
    }

    public MinValidator(int intValue) {
        super(intValue);
    }

    public MinValidator(long longValue) {
        super(longValue);
    }

    public MinValidator(float floatValue) {
        super(floatValue);
    }

    public MinValidator(double doubleValue) {
        super(doubleValue);
    }

    @Override
    public void validate(String name, String value) throws WebException {
        switch (super._numberType) {
            case SHORT:
                short sv = Short.parseShort(value);
                if (sv < super._shortValue) {
                    throw WebException.builder()
                            .errorCode(WebErrors.LESS_THEN_MIN_VALUE)
                            .variables(new WebErrors.LessThanMinValue()
                                    .name(name)
                                    .value(value)
                                    .limitation(super._shortValue)
                                    .type(super._numberType))
                            .build();
                }
                break;
            case INT:
                int iv = Integer.parseInt(value);
                if (iv < super._intValue) {
                    throw WebException.builder()
                            .errorCode(WebErrors.LESS_THEN_MIN_VALUE)
                            .variables(new WebErrors.LessThanMinValue()
                                    .name(name)
                                    .value(value)
                                    .limitation(super._intValue)
                                    .type(super._numberType))
                            .build();
                }
                break;
            case LONG:
                long lv = Long.parseLong(value);
                if (lv < super._longValue) {
                    throw WebException.builder()
                            .errorCode(WebErrors.LESS_THEN_MIN_VALUE)
                            .variables(new WebErrors.LessThanMinValue()
                                    .name(name)
                                    .value(value)
                                    .limitation(super._longValue)
                                    .type(super._numberType))
                            .build();
                }
                break;
            case FLOAT:
                float fv = Float.parseFloat(value);
                if (fv < super._floatValue) {
                    throw WebException.builder()
                            .errorCode(WebErrors.LESS_THEN_MIN_VALUE)
                            .variables(new WebErrors.LessThanMinValue()
                                    .name(name)
                                    .value(value)
                                    .limitation(super._floatValue)
                                    .type(super._numberType))
                            .build();
                }
                break;
            case DOUBLE:
                double dv = Double.parseDouble(value);
                if (dv < super._doubleValue) {
                    throw WebException.builder()
                            .errorCode(WebErrors.LESS_THEN_MIN_VALUE)
                            .variables(new WebErrors.LessThanMinValue()
                                    .name(name)
                                    .value(value)
                                    .limitation(super._doubleValue)
                                    .type(super._numberType))
                            .build();
                }
                break;
        }
    }
}
