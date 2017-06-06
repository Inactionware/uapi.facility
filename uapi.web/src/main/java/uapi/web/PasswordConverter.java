package uapi.web;

/**
 * Created by xquan on 6/5/2017.
 */
public class PasswordConverter implements IConverter<String, String> {

    @Override
    public String convert(String input) {
        return input;
    }
}
