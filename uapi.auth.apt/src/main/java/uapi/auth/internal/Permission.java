/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth.internal;

import uapi.auth.IPermission;
import uapi.resource.ResourceIdentify;

public class Permission implements IPermission {

    private final ResourceIdentify _resId;
    private final int _actions;

    public Permission(
            final String resourceId,
            final int actions
    ) {
        this._resId = ResourceIdentify.parse(resourceId);
        this._actions = actions;
    }

    @Override
    public ResourceIdentify resourceId() {
        return this._resId;
    }

    @Override
    public int actions() {
        return this._actions;
    }
}
