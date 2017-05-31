package uapi.web.http.annotation;

import uapi.web.http.BoolType;

/**
 * Indicate the data field need to verify by BooleanValidator
 */
public @interface BoolValidator {

    BoolType value() default BoolType.TrueFalse;
}
