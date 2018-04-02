/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import uapi.net.http.ContentType;
import uapi.net.http.HttpMethod;
import uapi.net.http.HttpVersion;
import uapi.net.http.IHttpRequest;

import java.util.List;
import java.util.Map;

public class NettyHttpRequest implements IHttpRequest {

    @Override
    public String peerAddress() {
        return null;
    }

    @Override
    public int peerPort() {
        return 0;
    }

    @Override
    public HttpVersion version() {
        return null;
    }

    @Override
    public HttpMethod method() {
        return null;
    }

    @Override
    public ContentType conentType() {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    @Override
    public Map<String, List<String>> params() {
        return null;
    }
}
