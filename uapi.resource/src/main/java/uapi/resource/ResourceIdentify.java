/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.resource;

import uapi.IPartibleIdentify;
import uapi.InvalidArgumentException;
import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;

public class ResourceIdentify implements IPartibleIdentify<String> {

    public static final String SEPARATOR   = "@";

    public static ResourceIdentify parse(
            final String resourceId
    ) {
        ArgumentChecker.required(resourceId, "resourceId");
        if (resourceId.indexOf(SEPARATOR) <= 0) {
            throw new InvalidArgumentException(resourceId, InvalidArgumentException.InvalidArgumentType.FORMAT);
        } else {
            String[] combined = resourceId.split(SEPARATOR);
            if (combined.length == 2) {
                return new ResourceIdentify(combined[0], combined[1]);
            } else {
                throw new InvalidArgumentException(resourceId, InvalidArgumentException.InvalidArgumentType.FORMAT);
            }
        }
    }

    public static String toString(
            final String name,
            final String type
    ) {
        ArgumentChecker.required(type, "type");
        if (ArgumentChecker.isEmpty(name)) {
            return StringHelper.makeString("{}{}", SEPARATOR, type);
        } else {
            return StringHelper.makeString("{}{}{}", name, SEPARATOR, type);
        }
    }

    private final String _name;
    private final String _type;
    private final String _id;

    private ResourceIdentify(
            final String type
    ) {
        this(null, type);
    }

    private ResourceIdentify(
            final String name,
            final String type
    ) {
        ArgumentChecker.required(type, "type");
        this._name = name;
        this._type = type;
        if (StringHelper.isNullOrEmpty(this._name)) {
            this._id = StringHelper.makeString("{}{}", SEPARATOR, this._type);
        } else {
            this._id = StringHelper.makeString("{}{}{}", this._name, SEPARATOR, this._type);
        }
    }

    @Override
    public Object[] getParts() {
        return new Object[] { this._name, this._type };
    }

    @Override
    public String getId() {
        return this._id;
    }

    public String getName() {
        return this._name;
    }

    public String getType() {
        return this._type;
    }

    public boolean isSpecificResource() {
        return ! ArgumentChecker.isNull(this._name);
    }

    @Override
    public String toString() {
        return this._id;
    }
}
