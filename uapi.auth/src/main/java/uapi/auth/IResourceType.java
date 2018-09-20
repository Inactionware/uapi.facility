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
 * A resource category
 */
public interface IResourceType {

    /**
     * Resource type name
     *
     * @return  Name of resource type
     */
    String name();

    /**
     * Add supported action on this resource type
     *
     * @param   action
     *          Supported action
     */
    void addAction(int action);

    /**
     * Return available actions on this resource type
     *
     * @return  Available actions
     */
    int availableActions();

    /**
     * Set resource load which used to load specific resource
     *
     * @param   loader
     *          The resource loader
     */
    void setLoader(IResourceLoader loader);

    /**
     * Find resource by specific resource id
     *
     * @param   id
     *          The resource id
     * @return  Resource object of null if no such resource exists
     */
    IResource findResource(String id);
}
