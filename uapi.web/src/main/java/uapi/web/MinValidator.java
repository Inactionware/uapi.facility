package uapi.web;

/**
 * Created by xquan on 6/5/2017.
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

    }
}
