package uapi.web.http.annotation;

/**
 * Indicate the data field need to verify by RegexpValidator
 */
public @interface Regexp {

    String value();
}
