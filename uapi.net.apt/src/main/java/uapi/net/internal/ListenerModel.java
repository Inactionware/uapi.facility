package uapi.net.internal;

import uapi.common.ArgumentChecker;
import uapi.net.NetListenerAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * The model is used for putting net listener's properties during compiling time
 */
public class ListenerModel {

    private final String _listenerType;

    private final String _listenerClassName;
    private final String _initializerClassName;

    private final List<NetListenerAttribute> _attributes;

    public ListenerModel(
            final String listenerType,
            final String listenerClassName,
            final String initializerClassName) {
        this._listenerType = listenerType;
        this._listenerClassName = listenerClassName;
        this._initializerClassName = initializerClassName;
        this._attributes = new ArrayList<>();
    }

    public String listenerType() {
        return this._listenerType;
    }

    public String initializerClassName() {
        return this._initializerClassName;
    }

    public String listenerClassName() {
        return this._listenerClassName;
    }

    public List<NetListenerAttribute> attributes() {
        return this._attributes;
    }

    public void addAttribute(final NetListenerAttribute attribute) {
        ArgumentChecker.required(attribute, "attribute");
        this._attributes.add(attribute);
    }
}
