/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource;

import uapi.IIdentifiable;

/**
 * A resourceId category
 */
public interface IResourceType extends IIdentifiable<String> {

    @Override
    default String getId() {
        return name();
    }

    void setName(String name);

    /**
     * Resource type name
     *
     * @return  Name of resourceId type
     */
    String name();

    /**
     * Add supported action on this resourceId type
     *
     * @param   action
     *          Supported action
     */
    void addAction(int action);

    /**
     * Return available actions on this resourceId type
     *
     * @return  Available actions
     */
    int availableActions();

    /**
     * Set resourceId load which used to load specific resourceId
     *
     * @param   loader
     *          The resourceId loader
     */
    void setLoader(IResourceLoader loader);

    /**
     * Find resourceId by specific resourceId id
     *
     * @param   id
     *          The resourceId id
     * @return  Resource object of null if no such resourceId exists
     */
    IResource findResource(String id);
}
