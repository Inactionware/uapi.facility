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
