package uapi.web.http;

/**
 * Created by xquan on 5/31/2017.
 */
public interface IConverter<I, O> {

    O convert(I input);
}
