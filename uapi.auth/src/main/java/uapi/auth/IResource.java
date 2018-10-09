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
 * The resourceId is manageable unit in authentication framework
 */
public interface IResource {

    /**
     * The resourceId id
     *
     * @return  resourceId id
     */
    String id();

    /**
     * The owner of the resourceId
     *
     * @return  The resourceId owner
     */
    String owner();

    /**
     * The resourceId is granted to other user
     *
     * @return  Grants
     */
    IGrant[] grants();
}
