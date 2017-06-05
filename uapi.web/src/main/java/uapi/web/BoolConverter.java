package uapi.web;

/**
 * Created by xquan on 6/5/2017.
 */
public class BoolConverter implements IConverter<String, Boolean> {

    private final BoolType _type;

    public BoolConverter(BoolType type) {
        this._type = type;
    }

    @Override
    public Boolean convert(String input) {
        return null;
    }
}
