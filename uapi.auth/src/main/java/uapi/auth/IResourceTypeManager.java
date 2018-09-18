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
 * A manager to mange resource related information
 */
public interface IResourceTypeManager {

    /**
     * Register new resource type
     *
     * @param   resourceTypeName
     *          Resource type name
     * @return  Resource type object
     */
    IResourceType register(String resourceTypeName);

    /**
     * Find resource type object by resource type name
     *
     * @param   resourceTypeName
     *          Resource type name
     * @return  Resource type object of null if no such resource type
     */
    IResourceType findResourceType(String resourceTypeName);

    /**
     * Register new resource load
     *
     * @param   loader
     *          The resource loader
     */
    void register(IResourceLoader loader);
}
