import uapi.IModulePortal;
import uapi.net.internal.NetModulePortal;

module uapi.net {
    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.event;

    exports uapi.net;

    provides IModulePortal with NetModulePortal;
}