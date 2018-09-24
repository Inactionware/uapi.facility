/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth.annotation;

import uapi.common.StringHelper;

import java.lang.annotation.*;

/**
 * Any Action annotated with Authenticate will make framework check the authentication before action executing
 * The annotation can be annotated on an Action only which means the Action must be authenticated before executing
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Authenticates.class)
public @interface Authenticate {

    /**
     * Resource type name
     *
     * @return  Resource type name
     */
    String resource();

    /**
     * Indicate specific resource id
     *
     * @return  Resource id
     */
    String resourceId() default StringHelper.EMPTY;

    /**
     * Required actions on the resource
     *
     * @return  actions
     */
    int requiredActions();
}
