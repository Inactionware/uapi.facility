/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth.internal;

import uapi.auth.IResourceLoader;
import uapi.auth.IResourceType;
import uapi.auth.IResourceTypeManager;
import uapi.service.IServiceLifecycle;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Optional;

import java.util.Map;

public class ResourceTypeManager implements IResourceTypeManager, IServiceLifecycle {

    @Inject
    @Optional
    protected Map<String, IResourceType> _resourceTypes;

    @Override
    public IResourceType register(String resourceTypeName) {
        return null;
    }

    @Override
    public IResourceType findResourceType(String resourceTypeName) {
        return null;
    }

    @Override
    public void register(IResourceLoader loader) {

    }

    @Override
    public void onDependencyInject(String serviceId, Object service) {

    }
}
