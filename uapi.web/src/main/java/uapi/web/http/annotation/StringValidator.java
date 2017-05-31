package uapi.web.http.annotation;

/**
 * Indicate the data field need to verify by StringValidator
 */
public @interface StringValidator {

    int min();

    int max();

    String[] invalid();

    String[] valid();
}
