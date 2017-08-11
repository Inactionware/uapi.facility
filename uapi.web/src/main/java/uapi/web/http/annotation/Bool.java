package uapi.web.http.annotation;

import uapi.web.BoolType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate the data field need to verify by BooleanValidator
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bool {

    BoolType value() default BoolType.TrueFalse;
}
