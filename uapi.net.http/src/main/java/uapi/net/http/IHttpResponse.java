package uapi.net.http;

public interface IHttpResponse {

    void setVersion(HttpVersion version);

    void setStatus(HttpStatus status);

    void setHeader(String key, String value);

    void write(String message);

    void flush();
}
