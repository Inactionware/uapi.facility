package uapi.web;

import uapi.GeneralException;

/**
 * The validator checks the bool request data field is valid or not
 */
public class BoolValidator implements IValidator {

    private static final String B_TRUE  = "true";
    private static final String B_FALSE = "false";
    private static final String B_YES   = "yes";
    private static final String B_NO    = "no";
    private static final String B_ON    = "on";
    private static final String B_OFF   = "off";

    private final BoolType _type;

    public BoolValidator(final BoolType type) {
        this._type = type;
    }

    @Override
    public void validate(
            final String value
    ) throws WebException {
        switch (this._type) {
            case TrueFalse:
                if (! B_TRUE.equalsIgnoreCase(value) && ! B_FALSE.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_TRUE_FALSE)
                            .variables(new WebErrors.InvalidParamTrueFalse()
                                .value(value))
                            .build();
                }
                break;
            case OnOff:
                if (! B_ON.equalsIgnoreCase(value) && ! B_OFF.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_ON_OFF)
                            .variables(new WebErrors.InvalidParamOnOff()
                                .value(value))
                            .build();
                }
                break;
            case YesNo:
                if (! B_YES.equalsIgnoreCase(value) && ! B_NO.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_YES_NO)
                            .variables(new WebErrors.InvalidParamYesNo()
                                .value(value))
                            .build();
                }
                break;
            default:
                throw new GeneralException("Unsupported BoolType - {}", this._type);
        }
    }
}
