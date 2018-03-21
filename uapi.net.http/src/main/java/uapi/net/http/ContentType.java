package uapi.net.http;

import uapi.GeneralException;
import uapi.rx.Looper;
import uapi.rx.NoItemException;

public enum ContentType {

    FORM_URLENCODED("application/x-www-form-urlencoded"),
    TEXT("text/plain"),
    JSON("application/json"),
    JAVASCRIPT("application/javascript"),
    XML("text/xml"),
    HTML("text/html");

    public static ContentType parse(String typeName) {
        try {
            return Looper.on(ContentType.values())
                    .filter(contentType -> contentType.typeName().equalsIgnoreCase(typeName))
                    .first();
        } catch (NoItemException ex) {
            throw new GeneralException("No ContentType can be mapped to {}", typeName);
        }
    }

    private final String _typeName;

    ContentType(String name) {
        this._typeName = name;
    }

    public String typeName() {
        return this._typeName;
    }
}
