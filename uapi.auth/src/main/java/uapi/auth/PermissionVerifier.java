/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

import uapi.protocol.ResourceProcessing;
import uapi.resource.*;
import uapi.rx.Looper;

public abstract class PermissionVerifier {

    protected abstract IPermission[] requiredPermissions();

    protected abstract IResourceTypeManager resourceTypeManager();

    protected void verify(
            final ResourceProcessing resourceProcessing
    ) {
        var request = resourceProcessing.originalRequest();
        var session = request.session();
        IUser user = session.get(SessionKeys.USER);
        if (user == null) {
            user = new Anonymous();
            session.set(SessionKeys.USER, user);
        }
        var username = user.name();
        var userPermissions = Looper.on(user.roles())
                .flatmap(role -> Looper.on(role.permissions()))
                .toList();
        Looper.on(requiredPermissions()).foreach(requiredPermission -> {
            final ResourceIdentify resId = requiredPermission.resourceId();
            final int reqActions = requiredPermission.actions();
            var permission = Looper.on(userPermissions)
                    .filter(userPermission -> userPermission.resourceId().getType().equals(resId.getType()))
                    .filter(userPermission -> (userPermission.actions() & reqActions) == reqActions)
                    .first();
            if (permission != null) {
                return;
            }
            if (requiredPermission.resourceId().isSpecificResource()) {
                // Check user permission on specific resourceId
                var resourceType = resourceTypeManager().findResourceType(resId.getType());
                var resource = resourceType.findResource(resId.getName());
                if (resource == null) {
                    throw AuthenticationException.builder()
                            .errorCode(AuthenticationErrors.RESOURCE_NOT_FOUNT)
                            .variables(new AuthenticationErrors.ResourceNotFound()
                                    .resourceId(resId.getName())
                                    .resourceType(resId.getType()))
                            .build();
                }
                if (! resource.owner().equals(username)) {
                    var grant = Looper.on(resource.grants())
                            .filter(resGrant -> resGrant.user().equals(username))
                            .filter(resGrant -> (resGrant.allowedActions() & reqActions) == reqActions)
                            .first();
                    if (grant != null) {
                        return;
                    }
                }
            }
            throw AuthenticationException.builder()
                    .errorCode(AuthenticationErrors.NO_PERMISSIONS_ON_RESOURCE)
                    .variables(new AuthenticationErrors.NoPermissionsOnResource()
                            .username(username)
                            .permission(reqActions)
                            .resourceId(resId))
                    .build();
        });
    }
}
