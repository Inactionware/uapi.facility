import uapi.IModulePortal;
import uapi.app.terminal.internal.AppTerminalModulePortal;

module uapi.app.terminal {
    requires static uapi.codegen;
    requires static uapi.service.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.config;
    requires uapi.event;
    requires uapi.behavior;
    requires uapi.app;
    requires com.google.common;

    exports uapi.app.terminal;
    exports uapi.app.terminal.generated to uapi.service;

    provides IModulePortal with AppTerminalModulePortal;
}