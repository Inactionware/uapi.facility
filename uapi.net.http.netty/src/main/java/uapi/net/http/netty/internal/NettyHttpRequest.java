/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import uapi.common.ArgumentChecker;
import uapi.net.http.*;
import uapi.rx.Looper;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NettyHttpRequest implements IHttpRequest {

    private static final ContentType DEFAULT_CONTENT_TYPE   = ContentType.TEXT;
    private static final Charset DEFAULT_CHARSET            = CharsetUtil.UTF_8;

    private final HttpRequest               _nettyHttpReq;
    private final HttpVersion               _httpVer;
    private final HttpMethod                _method;
    private final String                    _uri;
    private final String                    _path;
    private final ContentType               _contentType;
    private final Charset                   _charset;
    private final Map<String, String>       _headers;
    private final Map<String, List<String>> _params;

    NettyHttpRequest(final HttpRequest request) {
        ArgumentChecker.required(request, "request");
        this._nettyHttpReq = request;

        // Decode http version
        io.netty.handler.codec.http.HttpVersion ver = request.protocolVersion();
        if (io.netty.handler.codec.http.HttpVersion.HTTP_1_0.equals(ver)) {
            this._httpVer = HttpVersion.V_1_0;
        } else if (io.netty.handler.codec.http.HttpVersion.HTTP_1_1.equals(ver)) {
            this._httpVer = HttpVersion.V_1_1;
        } else {
            throw HttpException.builder()
                    .errorCode(HttpErrors.UNSUPPORTED_HTTP_VERSION)
                    .variables(ver.text())
                    .build();
        }

        // Decode http method
        io.netty.handler.codec.http.HttpMethod method = request.method();
        if (io.netty.handler.codec.http.HttpMethod.GET.equals(method)) {
            this._method = HttpMethod.GET;
        } else if (io.netty.handler.codec.http.HttpMethod.PUT.equals(method)) {
            this._method = HttpMethod.PUT;
        } else if (io.netty.handler.codec.http.HttpMethod.POST.equals(method)) {
            this._method = HttpMethod.POST;
        } else if (io.netty.handler.codec.http.HttpMethod.PATCH.equals(method)) {
            this._method = HttpMethod.PATCH;
        } else if (io.netty.handler.codec.http.HttpMethod.DELETE.equals(method)) {
            this._method = HttpMethod.DELETE;
        } else {
            throw HttpException.builder()
                    .errorCode(HttpErrors.UNSUPPORTED_HTTP_METHOD)
                    .variables(method.name())
                    .build();
        }

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
    public Charset charset() {
        return this._charset;
    }

    @Override
    public Iterator<Map.Entry<String, String>> headers() {
        return this._headers.entrySet().iterator();
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
    public List<String> param(final String key) {
        ArgumentChecker.required(key, "key");
        return this._params.get(key);
    }
}
