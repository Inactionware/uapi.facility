package uapi.net.http;

import uapi.net.IRequest;

import java.util.List;
import java.util.Map;

public interface IHttpRequest extends IRequest {

    HttpVersion version();

    HttpMethod method();

    ContentType conentType();

    Map<String, String> headers();

    Map<String, List<String>> params();
}
