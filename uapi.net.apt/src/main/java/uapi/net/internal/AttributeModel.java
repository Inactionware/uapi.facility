package uapi.net.internal;

public class AttributeModel {

    private String _name;
    private String _setterName;
    private String _type;
    private boolean _isRequired;

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getSetterName() {
        return this._setterName;
    }

    public void setSetterName(String setterName) {
        this._setterName = setterName;
    }

    public String getType() {
        return this._type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public boolean isRequired() {
        return this._isRequired;
    }

    public void setRequired(boolean isRequired) {
        this._isRequired = isRequired;
    }
}
