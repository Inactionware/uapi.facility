import uapi.IModulePortal;
import uapi.service.spring.internal.ServiceSpringModulePortal;

module uapi.service.spring {
    requires static uapi.codegen;
    requires static uapi.service.apt;
    requires static uapi.config.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.config;
    requires spring.context;

    exports uapi.service.spring;

    provides IModulePortal with ServiceSpringModulePortal;
}