package uapi.web;

/**
 * Created by xquan on 6/5/2017.
 */
public class MaxValidator extends NumberValidator {

    public MaxValidator(short shortValue) {
        super(shortValue);
    }

    public MaxValidator(int intValue) {
        super(intValue);
    }

    public MaxValidator(long longValue) {
        super(longValue);
    }

    public MaxValidator(float floatValue) {
        super(floatValue);
    }

    public MaxValidator(double doubleValue) {
        super(doubleValue);
    }

    @Override
    public void validate(String name, String value) throws WebException {

    }
}
