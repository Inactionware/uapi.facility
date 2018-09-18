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
 * The resource is manageable unit in authentication framework
 */
public interface IResource {

    /**
     * The resource id
     *
     * @return  resource id
     */
    String id();

    /**
     * The owner of the resource
     *
     * @return  The resource owner
     */
    String owner();

    /**
     * The resource is granted to other user
     *
     * @return  Grants
     */
    IGrant[] grants();
}
