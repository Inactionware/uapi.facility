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
import uapi.net.http.ContentType;
import uapi.net.http.HttpEvent;
import uapi.net.http.HttpMethod;
import uapi.net.http.IHttpRequest;
import uapi.protocol.IProtocol;
import uapi.protocol.IProtocolDecoder;
import uapi.protocol.IProtocolEncoder;
import uapi.service.annotation.Service;

@Service(IProtocol.class)
public class GraphQLProtocol implements IProtocol {

    private static final String TYPE    = "GraphQL";

    private final GraphQLDecoder _decoder = new GraphQLDecoder();
    private final GraphQLEncoder _encoder = new GraphQLEncoder();

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public boolean isSupport(INetEvent event) {
        if (! (event instanceof HttpEvent)) {
            return false;
        }
        var httpEvent = (HttpEvent) event;
        var httpReq = httpEvent.request();
        if (httpReq.method() == HttpMethod.GET && httpReq.hasParam("query")) {
            return true;
        }
        if (httpReq.method() == HttpMethod.POST &&
                (httpReq.contentType() == ContentType.JSON || httpReq.contentType() == ContentType.GRAPHQL)) {
            return true;
        }
        return false;
    }

    @Override
    public IProtocolDecoder decoder() {
        return this._decoder;
    }

    @Override
    public IProtocolEncoder encoder() {
        return this._encoder;
    }
}
