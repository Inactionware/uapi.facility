/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource;

import uapi.common.ArgumentChecker;
import uapi.resource.IResource;
import uapi.resource.IResourceLoader;
import uapi.resource.IResourceType;

public class ResourceType implements IResourceType {

    private String _name;
    private int _action = 0;
    private IResourceLoader _loader;

    public void setName(String name) {
        ArgumentChecker.required(name, "name");
        this._name = name;
    }

    @Override
    public String name() {
        return this._name;
    }

    @Override
    public void addAction(int action) {
        ArgumentChecker.checkInt(action, "action", 1, Integer.MAX_VALUE);
        this._action = this._action | action;
    }

    @Override
    public int availableActions() {
        return this._action;
    }

    @Override
    public void setLoader(IResourceLoader loader) {
        ArgumentChecker.required(loader, "loader");
        this._loader = loader;
    }

    @Override
    public IResource findResource(String id) {
        if (this._loader == null) {
            return null;
        }
        return this._loader.load(id);
    }
}
