/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource.internal;

import uapi.GeneralException;
import uapi.behavior.IResponsible;
import uapi.behavior.IResponsibleRegistry;
import uapi.common.ArgumentChecker;
import uapi.resource.*;
import uapi.rx.Looper;
import uapi.service.IServiceLifecycle;
import uapi.service.annotation.Inject;
import uapi.service.annotation.OnActivate;
import uapi.service.annotation.Optional;
import uapi.service.annotation.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(IResourceTypeManager.class)
public class ResourceTypeManager implements IResourceTypeManager, IServiceLifecycle {

    private final Map<String, IResourceType> _resTypes = new ConcurrentHashMap<>();

    private final Map<String, IResourceLoader> _resLoaders = new ConcurrentHashMap<>();

    @Inject
    protected IResponsibleRegistry _respReg;

    @OnActivate
    public void activate() {
        Looper.on(this._resTypes.values()).foreach(this::registerResourceBehavior);
    }

    @Inject
    @Optional
    @Override
    public void register(IResourceType resourceType) {
        ArgumentChecker.required(resourceType, "resourceType");
        registerResourceType(resourceType);
    }

    @Inject
    @Optional
    @Override
    public void register(IResourceLoader loader) {
        ArgumentChecker.required(loader, "loader");
        IResourceLoader existingLoader = this._resLoaders.get(loader.resourceTypeName());
        if (existingLoader != null) {
            throw ResourceException.builder()
                    .errorCode(ResourceErrors.DUPLICATED_RESOURCE_LOADER)
                    .variables(new ResourceErrors.DuplicatedResourceLoader()
                            .resourceTypeName(loader.resourceTypeName()))
                    .build();
        }
        this._resLoaders.put(loader.resourceTypeName(), loader);
    }

    @Override
    public IResourceType register(String resourceTypeName) {
        ArgumentChecker.required(resourceTypeName, "resourceTypeName");
        IResourceType resType = this._resTypes.get(resourceTypeName);
        if (resType != null) {
            throw ResourceException.builder()
                    .errorCode(ResourceErrors.DUPLICATED_RESOURCE_TYPE)
                    .variables(new ResourceErrors.DuplicatedResourceType()
                            .resourceTypeName(resourceTypeName))
                    .build();
        }
        resType = new ResourceType();
        resType.setName(resourceTypeName);
        this._resTypes.put(resourceTypeName, resType);
        return resType;
    }

    @Override
    public IResourceType findResourceType(String resourceTypeName) {
        ArgumentChecker.required(resourceTypeName, "resourceTypeName");
        return this._resTypes.get(resourceTypeName);
    }

    @Override
    public void onDependencyInject(String serviceId, Object service) {
        ArgumentChecker.required(service, "service");
        if (service instanceof IResourceType) {
            IResourceType resType = (IResourceType) service;
            registerResourceType(resType);
            registerResourceBehavior(resType);
        } else if (service instanceof IResourceLoader) {
            IResourceLoader resLoader = (IResourceLoader) service;
            this._resLoaders.put(resLoader.resourceTypeName(), resLoader);
        } else {
            throw new GeneralException(
                    "Inject unsupported service - id: {}, service: {}",
                    serviceId, service.getClass().getName());
        }
    }

    private void registerResourceType(IResourceType resType) {
        String resName = resType.name();
        if (this._resTypes.containsKey(resName)) {
            throw ResourceException.builder()
                    .errorCode(ResourceErrors.DUPLICATED_RESOURCE_TYPE)
                    .variables(new ResourceErrors.DuplicatedResourceType()
                            .resourceTypeName(resName))
                    .build();
        }
        this._resTypes.put(resName, resType);
    }

    private void registerResourceBehavior(IResourceType resType) {
        if (! (resType instanceof ICapable)) {
            return;
        }
        ICapable capable = (ICapable) resType;
        String resName = resType.name();
        IResponsible resp = this._respReg.register(resName);
        capable.initBehavior(resp);
    }
}
