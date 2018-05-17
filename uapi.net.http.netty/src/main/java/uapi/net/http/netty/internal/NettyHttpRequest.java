/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import uapi.common.ArgumentChecker;
import uapi.net.http.*;
import uapi.net.http.HttpMethod;
import uapi.net.http.HttpVersion;
import uapi.rx.Looper;

import java.nio.charset.Charset;
import java.util.*;

public class NettyHttpRequest implements IHttpRequest {

    private static final ContentType DEFAULT_CONTENT_TYPE   = ContentType.TEXT;
    private static final Charset DEFAULT_CHARSET            = CharsetUtil.UTF_8;
    private static final int DEFAULT_CONTENT_LENGTH         = 0;

    private final HttpRequest               _nettyHttpReq;
    private final HttpVersion               _httpVer;
    private final HttpMethod                _method;
    private final String                    _uri;
    private final String                    _path;
    private final ContentType               _contentType;
    private final int                       _contentLength;
    private final Charset                   _charset;
    private final Map<String, String>       _headers;
    private final Map<String, List<String>> _params;
    private final List<ByteBuf>             _bodyParts;

    private boolean                         _lastBody = false;

    NettyHttpRequest(final HttpRequest request) {
        ArgumentChecker.required(request, "request");
        this._nettyHttpReq = request;

        // Decode http version and method
        this._httpVer = HttpVersionConverter.toUapiVersion(request.protocolVersion());
        this._method = HttpMethodConverter.toUapiVersion(request.method());

        // Decode request uri
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        this._uri = queryStringDecoder.uri();
        this._params = queryStringDecoder.parameters();
        this._path = queryStringDecoder.path();

        // Decode http headers
        this._headers = new HashMap<>();
        io.netty.handler.codec.http.HttpHeaders httpHeaders = request.headers();
        Looper.on(httpHeaders.iteratorAsString())
                .foreach(entry -> this._headers.put(entry.getKey(), entry.getValue()));

        // Decode http content type and charset
        String strContentType = this._headers.get(HttpHeaderNames.CONTENT_TYPE.toString());
        if (strContentType == null) {
            this._contentType = DEFAULT_CONTENT_TYPE;
            this._charset = DEFAULT_CHARSET;
        } else {
            String[] contentTypeInfo = strContentType.split(";");
            this._contentType = ContentType.parse(contentTypeInfo[0].trim());
            if (contentTypeInfo.length == 1) {
                this._charset = DEFAULT_CHARSET;
            } else {
                this._charset = Looper.on(contentTypeInfo)
                        .map(info -> info.split("="))
                        .filter(kv -> kv.length == 2)
                        .filter(kv -> "charset".equalsIgnoreCase(kv[0].trim()))
                        .map(kv -> kv[1].trim())
                        .map(Charset::forName)
                        .first(CharsetUtil.UTF_8);
            }
        }

        String strContentLen = httpHeaders.get(HttpHeaderNames.CONTENT_LENGTH);
        if (strContentLen == null) {
            this._contentLength = DEFAULT_CONTENT_LENGTH;
        } else {
            this._contentLength = Integer.parseInt(strContentLen);
        }

        this._bodyParts = new ArrayList<>();
    }

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
        return this._httpVer;
    }

    @Override
    public HttpMethod method() {
        return this._method;
    }

    @Override
    public String uri() {
        return this._uri;
    }

    @Override
    public String path() {
        return this._path;
    }

    @Override
    public ContentType contentType() {
        return this._contentType;
    }

    @Override
    public int contentLength() {
        return this._contentLength;
    }

    @Override
    public Charset charset() {
        return this._charset;
    }

    @Override
    public Iterator<Map.Entry<String, String>> headers() {
        return this._headers.entrySet().iterator();
    }

    @Override
    public boolean hasHeader(final String key) {
        ArgumentChecker.required(key, "key");
        return this._headers.containsKey(key);
    }

    @Override
    public String header(final String key) {
        ArgumentChecker.required(key, "key");
        return this._headers.get(key);
    }

    @Override
    public Iterator<Map.Entry<String, List<String>>> params() {
        return this._params.entrySet().iterator();
    }

    @Override
    public boolean hasParam(final String key) {
        ArgumentChecker.required(key, "key");
        return this._params.containsKey(key);
    }

    @Override
    public List<String> param(final String key) {
        ArgumentChecker.required(key, "key");
        return this._params.get(key);
    }

    public void appendBody(HttpContent httpContent) {
        ByteBuf buffer = httpContent.content();
        if (buffer.isReadable()) {
            this._bodyParts.add(buffer);
        }
        if (httpContent instanceof LastHttpContent) {
            this._lastBody = true;
        }
    }

    @Override
    public boolean isKeepAlive() {
        return HttpUtil.isKeepAlive(this._nettyHttpReq);
    }
}
