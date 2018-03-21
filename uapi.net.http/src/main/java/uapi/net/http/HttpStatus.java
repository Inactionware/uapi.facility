package uapi.net.http;

public enum HttpStatus {

    OK(200),
    CREATED(201),
    ACCEPTED(202),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    CONFLICT(409),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int _code;

    HttpStatus(int code) {
        this._code = code;
    }

    public int getCode() {
        return this._code;
    }
}
