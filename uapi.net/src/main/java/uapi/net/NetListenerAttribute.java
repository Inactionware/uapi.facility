package uapi.net;

import uapi.common.ArgumentChecker;

/**
 * A net listener attribute is used to initialize net listener
 */
public final class NetListenerAttribute {

    private final String _name;
    private final boolean _isRequired;

    public NetListenerAttribute(
            final String name,
            final boolean isRequired
    ) {
        ArgumentChecker.required(name, "name");

        this._name = name;
        this._isRequired = isRequired;
    }

    /**
     * The attribute name
     *
     * @return  The name of attribute
     */
    public String name() {
        return this._name;
    }

    /**
     * Indicate the attribute is required
     *
     * @return  True means the attribute is required otherwise is optional
     */
    public boolean isRequired() {
        return this._isRequired;
    }
}
