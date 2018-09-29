/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.user.internal;

import uapi.auth.annotation.Resource;
import uapi.user.IRole;
import uapi.user.IUser;

import java.util.ArrayList;
import java.util.List;

@Resource(name = User.TYPE, availableActions = UserActions.ALL)
public class User implements IUser {

    public static final String TYPE = "User";

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
