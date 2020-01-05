import uapi.IModulePortal;
import uapi.protocol.graphql.internal.ProtocolGraphqlModulePortal;

module uapi.protocol.graphql {
    requires static uapi.codegen;
    requires static uapi.service.apt;

    requires uapi.common;
    requires uapi.exception;
    requires uapi.service;
    requires uapi.net;
    requires uapi.net.http;
    requires uapi.protocol;

    exports uapi.protocol.graphql.generated to uapi.service;

    provides IModulePortal with ProtocolGraphqlModulePortal;
}