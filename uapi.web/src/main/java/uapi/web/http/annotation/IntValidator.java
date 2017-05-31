package uapi.web.http.annotation;

/**
 * Indicate the data field need to verify by IntValidator
 */
public @interface IntValidator {

    int min();

    int max();

    int[] invalid();

    int[] valid();
}
