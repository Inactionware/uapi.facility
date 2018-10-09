/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A uapi.auth.annotation.Resource is a manageable unit for authentication framework.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {

    /**
     * Resource name
     *
     * @return  Resource name
     */
    String name();

    /**
     * Available action on this resourceId
     *
     * @return  Available actions
     */
    int availableActions();
}
