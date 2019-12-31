import uapi.IModulePortal;
import uapi.protocol.internal.ProtocolModulePortal;

module uapi.protocol {
    requires static uapi.codegen;
    requires static uapi.service.apt;
    requires static uapi.behavior.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.behavior;

    exports uapi.protocol;

    provides IModulePortal with ProtocolModulePortal;
}