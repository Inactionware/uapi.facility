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
 * A manager to mange resourceId related information
 */
public interface IResourceTypeManager {

    /**
     * Register new resourceId load
     *
     * @param   loader
     *          The resourceId loader
     */
    void register(IResourceLoader loader);

    /**
     * Register new resourceId type
     *
     * @param   resourceType
     *          The resourceId type
     */
    void register(IResourceType resourceType);

    /**
     * Register new resourceId type
     *
     * @param   resourceTypeName
     *          Resource type name
     * @return  Resource type object
     */
    IResourceType register(String resourceTypeName);

    /**
     * Find resourceId type object by resourceId type name
     *
     * @param   resourceTypeName
     *          Resource type name
     * @return  Resource type object of null if no such resourceId type
     */
    IResourceType findResourceType(String resourceTypeName);
}
