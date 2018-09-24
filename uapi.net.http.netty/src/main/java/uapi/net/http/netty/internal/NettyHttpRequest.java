/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;
import uapi.net.IPeer;
import uapi.net.http.*;
import uapi.net.http.HttpMethod;
import uapi.net.http.HttpVersion;

import java.nio.charset.Charset;
import java.util.*;

public class NettyHttpRequest implements IHttpRequest {

    private final NettyHttpRequestHead _head;
    private final IHttpRequestBody _body;

    NettyHttpRequest(final NettyHttpRequestHead head) {
        this(head, null);
    }

    NettyHttpRequest(
            final NettyHttpRequestHead head,
            final IHttpRequestBody body
    ) {
        ArgumentChecker.required(head, "head");
        this._head = head;
        this._body = body;
    }
//
//    @Override
//    public String peerAddress() {
//        return this._head.peerAddress();
//    }
//
//    @Override
//    public int peerPort() {
//        return this._head.peerPort();
//    }

    @Override
    public IPeer peer() {
        return null;
    }

    @Override
    public HttpVersion version() {
        return this._head.version();
    }

    @Override
    public HttpMethod method() {
        return this._head.method();
    }

    @Override
    public String uri() {
        return this._head.uri();
    }

    @Override
    public String path() {
        return this._head.path();
    }

    @Override
    public ContentType contentType() {
        return this._head.contentType();
    }

    @Override
    public int contentLength() {
        return this._head.contentLength();
    }

    @Override
    public Charset charset() {
        return this._head.charset();
    }

    @Override
    public Iterator<Map.Entry<String, String>> headers() {
        return this._head.headers();
    }

    @Override
    public boolean hasHeader(final String key) {
        ArgumentChecker.required(key, "key");
        return this._head.hasHeader(key);
    }

    @Override
    public String header(final String key) {
        ArgumentChecker.required(key, "key");
        return this._head.header(key);
    }

    @Override
    public Iterator<Map.Entry<String, List<String>>> params() {
        return this._head.params();
    }

    @Override
    public boolean hasParam(final String key) {
        ArgumentChecker.required(key, "key");
        return this._head.hasParam(key);
    }

    @Override
    public List<String> param(final String key) {
        ArgumentChecker.required(key, "key");
        return this._head.param(key);
    }

    @Override
    public boolean isKeepAlive() {
        return this._head.isKeepAlive();
    }

    @Override
    public IHttpRequestBody body() {
        return this._body;
    }

    @Override
    public String toString() {
        return StringHelper.makeString("HTTP Request [version:{}, method:{}, uri:{}]",
                this._head.version().name(), this._head.method(), this._head.uri());
    }
}
