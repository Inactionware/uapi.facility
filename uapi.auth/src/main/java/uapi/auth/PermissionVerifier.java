/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

import uapi.net.IRequest;
import uapi.protocol.ResourceProcessing;

public abstract class PermissionVerifier {

    protected abstract IPermission[] requiredPermissions();

    protected abstract IResourceTypeManager resourceTypeManager();

    protected boolean verify(
            final ResourceProcessing resourceProcessing
    ) {
        IRequest request = resourceProcessing.originalRequest();
        String user = request.peer().user();

        return false;
    }
}
