/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

import uapi.IIdentifiable;

/**
 * Resource loader is used to load specific resource from data store (like db, file system...)
 */
public interface IResourceLoader extends IIdentifiable<String> {

    @Override
    default String getId() {
        return resourceTypeName();
    }

    /**
     * Load resource by its id
     *
     * @param   id The resourceId
     * @return  Resource object or null if no such resource exists
     */
    IResource load(String id);

    /**
     * Indicate which is the resource type this loader can load
     *
     * @return  Resource type name
     */
    String resourceTypeName();
}
