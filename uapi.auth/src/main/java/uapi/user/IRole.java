package uapi.user;

import uapi.auth.IPermission;

public interface IRole {

    String GUEST = "__Guest";

    String name();

    IPermission[] permissions();
}
