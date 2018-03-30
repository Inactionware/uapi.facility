package uapi.protocol.graphql.internal;

import uapi.net.INetEvent;
import uapi.protocol.IProtocol;
import uapi.protocol.IProtocolDecoder;
import uapi.protocol.IProtocolEncoder;

public class GraphQLProtocol implements IProtocol {

    private static final String TYPE    = "GraphQL";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public boolean isSupport(INetEvent event) {
        return false;
    }

    @Override
    public IProtocolDecoder decoder() {
        return null;
    }

    @Override
    public IProtocolEncoder encoder() {
        return null;
    }
}
