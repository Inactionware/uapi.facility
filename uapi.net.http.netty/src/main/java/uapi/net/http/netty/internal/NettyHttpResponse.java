/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import uapi.common.ArgumentChecker;
import uapi.net.http.HttpStatus;
import uapi.net.http.HttpVersion;
import uapi.net.http.IHttpResponse;
import uapi.rx.Looper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class NettyHttpResponse implements IHttpResponse {

    private final ChannelHandlerContext _ctx;
    private final NettyHttpRequest      _nettyHttpReq;

    private HttpStatus _status;
    private HttpVersion _version;
    private Map<String, String> _headers;
    private StringBuilder _buffer;

    private AtomicBoolean _flashed = new AtomicBoolean(false);

    public NettyHttpResponse(
            final ChannelHandlerContext context,
            final NettyHttpRequest request
    ) {
        ArgumentChecker.required(context, "context");
        ArgumentChecker.required(request, "request");

        this._ctx = context;
        this._nettyHttpReq = request;
        this._version = request.version();
        this._status = HttpStatus.OK;
        this._headers = new HashMap<>();
        this._buffer = new StringBuilder();
    }

    @Override
    public void setVersion(HttpVersion version) {
        ArgumentChecker.required(version, "version");
        this._version = version;
    }

    @Override
    public void setStatus(HttpStatus status) {
        ArgumentChecker.required(status, "status");
        this._status = status;
    }

    @Override
    public void setHeader(String key, String value) {
        ArgumentChecker.required(key, "key");
        ArgumentChecker.required(value, "value");

        this._headers.put(key, value);
    }

    @Override
    public void write(String message) {
        this._buffer.append(message);
    }

    @Override
    public void flush() {
        // TODO: Need to support flush multiple time to support large response data
        if (this._flashed.get()) {
            return;
        }

        FullHttpMessage response = new DefaultFullHttpResponse(
                ConstantConverter.toNetty(this._version),
                HttpResponseStatus.valueOf(this._status.getCode()),
                Unpooled.copiedBuffer(this._buffer.toString(), CharsetUtil.UTF_8)
        );

        if (this._nettyHttpReq.isKeepAlive()) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        Looper.on(this._headers.entrySet())
                .foreach(entry -> response.headers().set(entry.getKey(), entry.getValue()));
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        this._headers.clear();
        this._buffer.delete(0, this._buffer.length());

        this._ctx.writeAndFlush(response);
        this._flashed.set(true);
    }

    @Override
    public void close() {
        if (! this._flashed.get()) {
            flush();
        }

        this._ctx.close();
    }
}
