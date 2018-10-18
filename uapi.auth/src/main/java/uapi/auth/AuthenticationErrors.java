/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

import uapi.common.CollectionHelper;
import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;
import uapi.resource.ResourceIdentify;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationErrors extends FileBasedExceptionErrors<AuthenticationException> {

    public static final int CATEGORY    = 0x0220;

    private static final Map<Integer, String> keyCodeMapping;

    public static final int RESOURCE_NOT_FOUNT              = 1;
    public static final int NO_PERMISSIONS_ON_RESOURCE      = 2;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(RESOURCE_NOT_FOUNT, ResourceNotFound.KEY);
        keyCodeMapping.put(NO_PERMISSIONS_ON_RESOURCE, NoPermissionsOnResource.KEY);
    }

    @Override
    protected String getFile(AuthenticationException exception) {
        if (exception.category() == CATEGORY) {
            return "/authenticationErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(AuthenticationException exception) {
        return keyCodeMapping.get(exception.errorCode());
    }

    /**
     * Error string template:
     *      Can't find resourceId by id {} on resourceId type {}
     */
    public static final class ResourceNotFound extends IndexedParameters<ResourceNotFound> {

        private static final String KEY = "ResourceNotFound";

        private String _resId;
        private String _resType;

        public ResourceNotFound resourceId(String id) {
            this._resId = id;
            return this;
        }

        public ResourceNotFound resourceType(String type) {
            this._resType = type;
            return this;
        }

        @Override
        public Object[] get() {
            return CollectionHelper.newObjectArray(this._resId, this._resType);
        }
    }

    /**
     * Error string template:
     *      The user {} has no permissions {} on resourceId - {}
     */
    public static final class NoPermissionsOnResource extends IndexedParameters<NoPermissionsOnResource> {

        private static final String KEY = "NoPermissionsOnResource";

        private String _username;
        private int _permissions;
        private ResourceIdentify _resId;

        public NoPermissionsOnResource username(String name) {
            this._username = name;
            return this;
        }

        public NoPermissionsOnResource permission(int permissions) {
            this._permissions = permissions;
            return this;
        }

        public NoPermissionsOnResource resourceId(ResourceIdentify resourceId) {
            this._resId = resourceId;
            return this;
        }

        @Override
        public Object[] get() {
            return CollectionHelper.newObjectArray( this._username, this._permissions, this._resId.toString() );
        }
    }
}
