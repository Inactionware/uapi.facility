package uapi.net.http;

import uapi.net.IResponse;

public interface IHttpResponse extends IResponse {

    void setVersion(HttpVersion version);

    void setStatus(HttpStatus status);

    void setHeader(String key, String value);

    void write(String message);

    void flush();
}
