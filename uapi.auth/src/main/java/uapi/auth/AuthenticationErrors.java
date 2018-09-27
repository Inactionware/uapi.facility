/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationErrors extends FileBasedExceptionErrors<AuthenticationException> {

    public static final int CATEGORY    = 0x0220;

    private static final Map<Integer, String> keyCodeMapping;

    public static final int RESOURCE_NOT_FOUNT              = 1;
    public static final int NO_PERMISSION_ON_RESOURCE       = 2;
    public static final int NO_PERMISSION_ON_RESOURCE_TYPE  = 3;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(RESOURCE_NOT_FOUNT, ResourceNotFound.KEY);
        keyCodeMapping.put(NO_PERMISSION_ON_RESOURCE, NoPermissionOnResource.KEY);
        keyCodeMapping.put(NO_PERMISSION_ON_RESOURCE_TYPE, NoPermissionOnResourceType.KEY);
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
     *      Can't find resource by id {} on resource type {}
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
            return new Object[] { this._resId, this._resType };
        }
    }

    /**
     * Error string template:
     *      The user {} has no permission {} on resource id {} of resource type {}
     */
    public static final class NoPermissionOnResource extends IndexedParameters<NoPermissionOnResource> {

        private static final String KEY = "NoPermissionOnResource";

        private String _username;
        private int _permission;
        private String _resId;
        private String _resType;

        public NoPermissionOnResource username(String name) {
            this._username = name;
            return this;
        }

        public NoPermissionOnResource permission(int permission) {
            this._permission = permission;
            return this;
        }

        public NoPermissionOnResource resourceId(String id) {
            this._resId = id;
            return this;
        }

        public NoPermissionOnResource resourceType(String type) {
            this._resType = type;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._username, this._permission, this._resId, this._resType };
        }
    }

    /**
     * Error string template:
     *      The user {} has no permission {} on resource type {}
     */
    public static final class NoPermissionOnResourceType extends IndexedParameters<NoPermissionOnResourceType> {

        private static final String KEY = "NoPermissionOnResourceType";

        private String _username;
        private int _permission;
        private String _resType;

        public NoPermissionOnResourceType username(String name) {
            this._username = name;
            return this;
        }

        public NoPermissionOnResourceType permission(int permission) {
            this._permission = permission;
            return this;
        }

        public NoPermissionOnResourceType resourceType(String type) {
            this._resType = type;
            return this;
        }

        @Override
        public Object[] get() {
            return new Object[] { this._username, this._permission, this._resType };
        }
    }
}
