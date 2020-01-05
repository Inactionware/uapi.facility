import uapi.IModulePortal;
import uapi.behavior.common.internal.BehaviorCommonModulePortal;

module uapi.behavior.common {
    requires static uapi.codegen;
    requires static uapi.service.apt;
    requires static uapi.behavior.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.behavior;

    exports uapi.behavior.common.action;
    exports uapi.behavior.common.generated to uapi.service;

    provides IModulePortal with BehaviorCommonModulePortal;
}