package uapi.web;

import uapi.web.WebException;

/**
 * Created by xquan on 5/31/2017.
 */
public interface IValidator {

    void validate(String value) throws WebException;
}
