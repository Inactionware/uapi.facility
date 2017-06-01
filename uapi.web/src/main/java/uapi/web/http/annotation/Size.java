package uapi.web.http.annotation;

/**
 * Indicate the data field need to verify by SizeValidator
 */
public @interface Size {

    int min();

    int max();
}
