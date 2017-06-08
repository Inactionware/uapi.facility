package uapi.web;

import uapi.GeneralException;

/**
 * The validator checks the bool request data field is valid or not
 */
public class BoolValidator implements IValidator {

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
                if (! BoolType.TRUE.equalsIgnoreCase(value) && ! BoolType.FALSE.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_TRUE_FALSE)
                            .variables(new WebErrors.InvalidParamTrueFalse()
                                    .name(name)
                                    .value(value))
                            .build();
                }
                break;
            case OnOff:
                if (! BoolType.ON.equalsIgnoreCase(value) && ! BoolType.OFF.equalsIgnoreCase(value)) {
                    throw WebException.builder()
                            .errorCode(WebErrors.INVALID_PARAM_ON_OFF)
                            .variables(new WebErrors.InvalidParamOnOff()
                                    .name(name)
                                    .value(value))
                            .build();
                }
                break;
            case YesNo:
                if (! BoolType.YES.equalsIgnoreCase(value) && ! BoolType.NO.equalsIgnoreCase(value)) {
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
