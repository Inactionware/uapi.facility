/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource;

import uapi.common.CollectionHelper;
import uapi.exception.FileBasedExceptionErrors;
import uapi.exception.IndexedParameters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceErrors extends FileBasedExceptionErrors<ResourceException> {

    public static final int CATEGORY    = 0x0230;

    private static final Map<Integer, String> keyCodeMapping;

    public static final int DUPLICATED_RESOURCE_TYPE        = 1;
    public static final int DUPLICATED_RESOURCE_LOADER      = 2;

    static {
        keyCodeMapping = new ConcurrentHashMap<>();
        keyCodeMapping.put(DUPLICATED_RESOURCE_TYPE, DuplicatedResourceType.KEY);
        keyCodeMapping.put(DUPLICATED_RESOURCE_LOADER, DuplicatedResourceLoader.KEY);
    }

    @Override
    protected String getFile(ResourceException exception) {
        if (exception.category() == CATEGORY) {
            return "/resourceErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(ResourceException exception) {
        return keyCodeMapping.get(exception.errorCode());
    }

    /**
     * Error string template:
     *      The resourceId type was registered in the rep - {}
     */
    public static final class DuplicatedResourceType extends IndexedParameters<DuplicatedResourceType> {

        private static final String KEY = "DuplicatedResourceType";

        private String _resTypeName;

        public DuplicatedResourceType resourceTypeName(String typeName) {
            this._resTypeName = typeName;
            return this;
        }

        @Override
        public Object[] get() {
            return CollectionHelper.newObjectArray(this._resTypeName);
        }
    }

    /**
     * Error string template:
     *      The resourceId loader was registered in the repo - {}
     */
    public static final class DuplicatedResourceLoader extends IndexedParameters<DuplicatedResourceLoader> {

        private static final String KEY = "DuplicatedResourceLoader";

        private String _resTypeName;

        public DuplicatedResourceLoader resourceTypeName(String typeName) {
            this._resTypeName = typeName;
            return this;
        }

        @Override
        public Object[] get() {
            return CollectionHelper.newObjectArray(this._resTypeName);
        }
    }
}
