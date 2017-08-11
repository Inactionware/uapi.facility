package uapi.web;

/**
 * Created by xquan on 6/5/2017.
 */
public abstract class NumberValidator implements IValidator {

    protected NumberType    _numberType;
    protected short         _shortValue   = Short.MIN_VALUE;
    protected int           _intValue     = Integer.MIN_VALUE;
    protected long          _longValue    = Long.MIN_VALUE;
    protected float         _floatValue   = Float.MIN_VALUE;
    protected double        _doubleValue  = Double.MIN_VALUE;

    public NumberValidator(short shortValue) {
        this._numberType = NumberType.SHORT;
        this._shortValue = shortValue;
    }

    public NumberValidator(int intValue) {
        this._numberType = NumberType.INT;
        this._intValue = intValue;
    }

    public NumberValidator(long longValue) {
        this._numberType = NumberType.LONG;
        this._longValue = longValue;
    }

    public NumberValidator(float floatValue) {
        this._numberType = NumberType.FLOAT;
        this._floatValue = floatValue;
    }

    public NumberValidator(double doubleValue) {
        this._numberType = NumberType.DOUBLE;
        this._doubleValue = doubleValue;
    }

    public enum NumberType {
        SHORT, INT, LONG, FLOAT, DOUBLE
    }
}
