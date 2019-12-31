import uapi.IModulePortal;
import uapi.user.internal.UserModulePortal;

module uapi.user {
    requires static uapi.codegen;
    requires static uapi.service.apt;
    requires static uapi.behavior.apt;
    requires static uapi.resource.apt;
    requires static uapi.auth.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.behavior;
    requires uapi.resource;
    requires uapi.auth;
    requires uapi.protocol;

    exports uapi.user;

    provides IModulePortal with UserModulePortal;
}