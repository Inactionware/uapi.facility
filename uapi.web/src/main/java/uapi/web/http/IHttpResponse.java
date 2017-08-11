package uapi.web.http;

import java.io.IOException;

/**
 * Created by xquan on 5/26/2017.
 */
public interface IHttpResponse {

    void output(String message) throws IOException;

    void output(String messageTemplate, String... args) throws IOException;

    void complete() throws IOException;
}
