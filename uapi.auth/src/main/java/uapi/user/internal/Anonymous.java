package uapi.user.internal;

import uapi.user.IRole;
import uapi.user.IUser;

public class Anonymous implements IUser {

    @Override
    public String name() {
        return ANONYMOUS;
    }

    @Override
    public IRole[] roles() {
        return new IRole[0];
    }
}
