package uapi.web;

import uapi.GeneralException;

/**
 * The validator checks the bool request data field is valid or not
 */
public class BoolValidator implements IValidator {

    public static final String B_TRUE  = "true";
    public static final String B_FALSE = "false";
    public static final String B_YES   = "yes";
    public static final String B_NO    = "no";
    public static final String B_ON    = "on";
    public static final String B_OFF   = "off";

    private final BoolType _type;

    public BoolValidator(final BoolType type) {
        this._type = type;
    }

    @Override
    public void validate(
            final String name,
            final String value
    ) throws WebException {
        switch (this._type) {
            case TrueFalse:
                if (! B_TRUE.equalsIgnoreCase(value) && ! B_FALSE.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_TRUE_FALSE)
                            .variables(new WebErrors.InvalidParamTrueFalse()
                                    .name(name)
                                    .value(value))
                            .build();
                }
                break;
            case OnOff:
                if (! B_ON.equalsIgnoreCase(value) && ! B_OFF.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_ON_OFF)
                            .variables(new WebErrors.InvalidParamOnOff()
                                    .name(name)
                                    .value(value))
                            .build();
                }
                break;
            case YesNo:
                if (! B_YES.equalsIgnoreCase(value) && ! B_NO.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_YES_NO)
                            .variables(new WebErrors.InvalidParamYesNo()
                                    .name(name)
                                    .value(value))
                            .build();
                }
                break;
            default:
                throw new GeneralException("Unsupported BoolType - {}", this._type);
        }
    }
}
