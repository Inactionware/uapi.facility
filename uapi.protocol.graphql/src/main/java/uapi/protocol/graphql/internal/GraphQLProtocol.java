/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

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
