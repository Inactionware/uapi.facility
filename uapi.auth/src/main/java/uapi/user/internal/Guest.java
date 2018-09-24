package uapi.user.internal;

import uapi.auth.IPermission;
import uapi.user.IRole;

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
