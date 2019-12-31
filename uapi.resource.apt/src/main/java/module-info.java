module uapi.resource.apt {
    requires auto.service.annotations;
    requires uapi.codegen;
    requires uapi.service.apt;
    requires uapi.behavior.apt;
    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.behavior;
    requires uapi.resource;

    exports uapi.resource.annotation;
}