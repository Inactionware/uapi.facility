import uapi.IModulePortal;
import uapi.net.telnet.internal.NetTelnetModulePortal;

module uapi.telnet {
    requires uapi.codegen;
    requires uapi.service.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.net;
    requires netty.all;

    exports uapi.net.telnet;

    provides IModulePortal with NetTelnetModulePortal;
}