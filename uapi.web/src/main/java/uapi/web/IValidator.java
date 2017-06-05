package uapi.web;

/**
 * Created by xquan on 5/31/2017.
 */
public interface IValidator {

    void validate(String name, String value) throws WebException;
}
