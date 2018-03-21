package uapi.net.http;

import java.util.List;
import java.util.Map;

public interface IHttpRequest {

    HttpVersion version();

    HttpMethod method();

    ContentType conentType();

    Map<String, String> headers();

    Map<String, List<String>> params();
}
