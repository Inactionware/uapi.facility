import uapi.IModulePortal;
import uapi.net.http.netty.internal.NetHttpNettyModulePortal;

module uapi.net.http.netty {
    requires static uapi.codegen;
    requires static uapi.service.apt;
    requires static uapi.config.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.state;
    requires uapi.service;
    requires uapi.config;
    requires netty.all;

    provides IModulePortal with NetHttpNettyModulePortal;
}