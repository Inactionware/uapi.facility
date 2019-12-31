import uapi.IModulePortal;
import uapi.protocol.graphql.internal.ProtocolGraphqlModulePortal;

module uapi.protocol.graphql {
    requires static uapi.codegen;
    requires static uapi.service.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.net;
    requires uapi.protocol;

    provides IModulePortal with ProtocolGraphqlModulePortal;
}