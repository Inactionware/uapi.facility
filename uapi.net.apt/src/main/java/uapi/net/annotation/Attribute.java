package uapi.net.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate the specific field is used for attribute of net listener
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {

    /**
     * The attribute name
     *
     * @return  Name of attribute
     */
    String value();

    /**
     * The attribute is required
     *
     * @return  The attribute is required when create new listener
     */
    boolean isRequired() default false;
}
