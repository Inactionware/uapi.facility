module uapi.auth.apt {
    requires java.compiler;
    requires freemarker;
    requires auto.service.annotations;
    requires uapi.codegen;
    requires uapi.service.apt;
    requires uapi.behavior.apt;
    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.behavior;
    requires uapi.resource;
    requires uapi.auth;

    exports uapi.auth.annotation;
}