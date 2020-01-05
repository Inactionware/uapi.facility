import uapi.IModulePortal;
import uapi.auth.internal.AuthModulePortal;

module uapi.auth {
    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.behavior;
    requires uapi.resource;
    requires uapi.protocol;
    requires uapi.net;

    exports uapi.auth;

    provides IModulePortal with AuthModulePortal;
}