/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.user;

import uapi.auth.annotation.Authenticate;
import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.protocol.ResourceProcessing;
import uapi.service.annotation.Service;

@Service
@Action
@Authenticate(resourceId = User.ID, requiredActions = UserActions.SIGNIN)
@Authenticate(resourceId = User.ID, requiredActions = UserActions.SIGNUP)
public class SignInUser {

    @ActionDo
    public ResourceProcessing signIn(ResourceProcessing resourceProcessing) {
        return resourceProcessing;
    }
}
