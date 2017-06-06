package uapi.web;

import uapi.GeneralException;

/**
 * Convert boolean string to boolean type
 */
public class BoolConverter implements IConverter<String, Boolean> {

    private final BoolType _type;

    public BoolConverter(BoolType type) {
        this._type = type;
    }

    @Override
    public Boolean convert(String input) {
        Boolean result;
        switch (this._type) {
            case TrueFalse:
                result = BoolValidator.B_TRUE.equalsIgnoreCase(input);
                break;
            case OnOff:
                result = BoolValidator.B_ON.equalsIgnoreCase(input);
                break;
            case YesNo:
                result = BoolValidator.B_YES.equalsIgnoreCase(input);
                break;
            default:
                // The case should not happen
                throw new GeneralException("Unsupported bool type - {}", this._type);
        }
        return result;
    }
}
