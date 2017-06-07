package uapi.web.http;

import java.util.Map;

/**
 * Created by xquan on 5/26/2017.
 */
public interface IHttpRequest {

    String url();

    Map<String, String[]> parameters();

    String parameter(String name);

    String header(String name);
}
