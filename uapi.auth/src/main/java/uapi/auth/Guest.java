/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth;

public class Guest implements IRole {

    @Override
    public String name() {
        return GUEST;
    }

    @Override
    public IPermission[] permissions() {
        return new IPermission[0];
    }
}
