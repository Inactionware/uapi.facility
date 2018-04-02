/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol;

import uapi.common.ArgumentChecker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResourceOperation {

    private final String _resType;
    private final String _resId;
    private final int _opType;
    private final Map<String, Object> _params;

    public ResourceOperation(
            final String resourceType,
            final String resourceId,
            final int operationType
    ) {
        ArgumentChecker.required(resourceType, "resourceType");

        this._resType = resourceType;
        this._resId = resourceId;
        this._opType = operationType;
        this._params = new HashMap<>();
    }

    public void addParameter(
            final String key,
            final Object value
    ) {
        ArgumentChecker.required(key, "key");

        this._params.put(key, value);
    }

    public Iterator<Map.Entry<String,Object>> parameterIterator() {
        return this._params.entrySet().iterator();
    }

    public String resourceType() {
        return this._resType;
    }

    public String resourceId() {
        return this._resId;
    }

    public int operationType() {
        return this._opType;
    }


}
