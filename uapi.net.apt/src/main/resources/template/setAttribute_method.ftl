uapi.common.ArgumentChecker.required(name, "name");
        uapi.common.ArgumentChecker.required(value, "value");

        if (this._attributes.containsKey(name)) {
            throw new uapi.GeneralException("Listener attribute can set only one time - {}", name);
        }
        this._attributes.put(name, value);
        return this;