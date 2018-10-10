/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.user;

import uapi.auth.ResourceIdentify;
import uapi.auth.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Resource(type = User.TYPE, availableActions = UserActions.ALL)
public class User implements IUser {

    public static final String TYPE = "User";
    public static final String ID   = ResourceIdentify.SEPARATOR + TYPE;

    private String _name;
    private List<IRole> _roles = new ArrayList<>();

    @Override
    public String name() {
        return this._name;
    }

    @Override
    public IRole[] roles() {
        return this._roles.toArray(new IRole[0]);
    }
}
