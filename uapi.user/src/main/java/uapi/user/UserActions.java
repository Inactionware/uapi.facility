/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.user;

import uapi.auth.BasicActions;

public class UserActions extends BasicActions {

    public static final int SIGNUP  = 0x10;
    public static final int SIGNIN  = 0x11;
    public static final int SIGNOUT = 0x12;

    public static final int ALL     = BASIC_ALL + SIGNUP + SIGNIN + SIGNOUT;

    private UserActions() {
        super();
    }
}
