import uapi.IModulePortal;
import uapi.resource.internal.ResourceModulePortal;

module uapi.resource {
    requires static uapi.codegen;
    requires static uapi.service.apt;
    requires static uapi.behavior.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.event;
    requires uapi.behavior;

    exports uapi.resource;
    exports uapi.resource.generated to uapi.service;

    provides IModulePortal with ResourceModulePortal;
}