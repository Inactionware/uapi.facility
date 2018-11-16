/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource.annotation;

import uapi.common.StringHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation indicate the method will contribute behavior for the resource.
 * The annotation only can be put on the method which is belong to a Resource class.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Behavior {

    /**
     * The event topic which will trigger the behavior.
     * The annotated method must has void return type and has only one argument which type is IBehavior
     *
     * @return  The event topic
     */
    String value() default StringHelper.EMPTY;
}
