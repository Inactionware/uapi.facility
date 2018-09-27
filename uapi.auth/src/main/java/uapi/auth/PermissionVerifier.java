/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

import uapi.common.StringHelper;
import uapi.net.IRequest;
import uapi.net.ISession;
import uapi.protocol.ResourceProcessing;
import uapi.rx.Looper;
import uapi.user.IUser;
import uapi.user.internal.Anonymous;

import java.util.List;

public abstract class PermissionVerifier {

//    protected abstract IPermission[] requiredPermissions();

    protected abstract IResourceTypeManager resourceTypeManager();

    protected void verify(
            final ResourceProcessing resourceProcessing
    ) {
        IRequest request = resourceProcessing.originalRequest();
        ISession session = request.session();
        IUser user = session.get(SessionKeys.USER);
        if (user == null) {
            user = new Anonymous();
            session.set(SessionKeys.USER, user);
        }
        String username = user.name();
        List<IPermission> userPermissions = Looper.on(user.roles())
                .flatmap(role -> Looper.on(role.permissions()))
                .toList();
        Looper.on(resourceProcessing.operationIterator()).foreach(resourceOp -> {
            String resId = resourceOp.resourceId();
            String resType = resourceOp.resourceType();
            int opType = resourceOp.operationType();
            if (! StringHelper.isNullOrEmpty(resId)) {
                // Check user permission on the resource type
                IPermission permission = Looper.on(userPermissions)
                        .filter(userPermission -> userPermission.resourceType().equals(resType))
                        .filter(userPermission -> (userPermission.actions() & opType) == opType)
                        .first();
                if (permission != null) {
                    return;
                }

                // Check user permission on specific resource
                IResourceType resourceType = resourceTypeManager().findResourceType(resType);
                IResource resource = resourceType.findResource(resId);
                if (resource == null) {
                    throw AuthenticationException.builder()
                            .errorCode(AuthenticationErrors.RESOURCE_NOT_FOUNT)
                            .variables(new AuthenticationErrors.ResourceNotFound()
                                    .resourceId(resId)
                                    .resourceType(resType))
                            .build();
                }
                if (! resource.owner().equals(username)) {
                    IGrant grant = Looper.on(resource.grants())
                            .filter(resGrant -> resGrant.user().equals(username))
                            .filter(resGrant -> (resGrant.allowedActions() & opType) == opType)
                            .first();
                    if (grant == null) {
                        throw AuthenticationException.builder()
                                .errorCode(AuthenticationErrors.NO_PERMISSION_ON_RESOURCE)
                                .variables(new AuthenticationErrors.NoPermissionOnResource()
                                        .username(username)
                                        .permission(opType)
                                        .resourceId(resId)
                                        .resourceType(resType))
                                .build();
                    }
                }
            } else {
                IPermission permission = Looper.on(userPermissions)
                        .filter(userPermission -> userPermission.resourceType().equals(resType))
                        .filter(userPermission -> (userPermission.actions() & opType) == opType)
                        .first();
                if (permission == null) {
                    throw AuthenticationException.builder()
                            .errorCode(AuthenticationErrors.NO_PERMISSION_ON_RESOURCE_TYPE)
                            .variables(new AuthenticationErrors.NoPermissionOnResourceType()
                                    .username(username)
                                    .permission(opType)
                                    .resourceType(resType))
                            .build();
                }
            }
        });
    }
}
