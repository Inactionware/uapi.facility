/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

/**
 * A uapi.auth.Resource is a manageable unit for authentication framework.
 */
public @interface Resource {

    /**
     * Resource name
     *
     * @return  Resource name
     */
    String name();

    /**
     * Available action on this resource
     *
     * @return  Available actions
     */
    int availableActions();
}
