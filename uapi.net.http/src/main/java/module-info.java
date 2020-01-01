import uapi.IModulePortal;
import uapi.net.http.internal.NetHttpModulePortal;

module uapi.net.http {
    requires static uapi.codegen;
    requires static uapi.service.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.state;
    requires uapi.service;
    requires uapi.behavior;
    requires uapi.net;

    exports uapi.net.http;

    provides IModulePortal with NetHttpModulePortal;
}