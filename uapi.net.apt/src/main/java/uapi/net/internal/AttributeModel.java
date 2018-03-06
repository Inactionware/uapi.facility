package uapi.net.internal;

public class AttributeModel {

    private String _name;
    private String _fieldName;
    private String _fieldType;
    private boolean _isRequired;

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getFieldName() {
        return this._fieldName;
    }

    public void setFieldName(String fieldName) {
        this._fieldName = fieldName;
    }

    public String getFieldType() {
        return this._fieldType;
    }

    public void setFieldType(String type) {
        this._fieldType = type;
    }

    public Boolean isRequired() {
        return this._isRequired;
    }

    public void setRequired(boolean isRequired) {
        this._isRequired = isRequired;
    }
}
