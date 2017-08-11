package uapi.web;

/**
 * The validator checks the integer request data is valid or not
 */
public class SizeValidator implements IValidator {

    private final int _min;

    private final int _max;

    public SizeValidator(int min, int max) {
        this._min = min;
        this._max = max;
    }

    @Override
    public void validate(String name, String value) throws WebException {
        int size;
        if (value == null) {
            size = 0;
        } else {
            size = value.length();
        }
        if (size < this._min || size > this._max) {
            throw WebException.builder()
                    .errorCode(WebErrors.SIZE_OVER_LIMITATION)
                    .variables(new WebErrors.SizeOverLimitation()
                            .name(name)
                            .value(value)
                            .min(this._min)
                            .max(this._max))
                    .build();
        }
    }
}
