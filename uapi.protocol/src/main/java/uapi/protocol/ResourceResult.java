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

public class ResourceResult {

    private final Map<String, Object> _results;

    public ResourceResult() {
        this._results = new HashMap<>();
    }

    public void addResult(
            final String key,
            final Object result
    ) {
        ArgumentChecker.required(key, "key");

        this._results.put(key, result);
    }

    public Iterator<Map.Entry<String, Object>> resultIterator() {
        return this._results.entrySet().iterator();
    }
}
