package uapi.net.http.netty.internal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import uapi.common.Capacity;
import uapi.event.IEventBus;
import uapi.net.http.HttpEvent;
import uapi.net.http.HttpException;
import uapi.net.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private final IEventBus _eventBus;
    private final String _eventSource;

    private final Capacity _reqBodyBufSize;

    private NettyHttpRequestHead _reqHead;

    private boolean _fragmentBody = false;
    private int _bodySize = 0;
    private final List<ByteBuf> _bodyBuffer = new ArrayList<>();

    HttpRequestHandler(
            final IEventBus eventBus,
            final String eventSource,
            final Capacity requestBodyBufferSize
    ) {
        this._eventBus = eventBus;
        this._eventSource = eventSource;
        this._reqBodyBufSize = requestBodyBufferSize;
    }

    @Override
    public void channelRead(
            final ChannelHandlerContext channelCtx,
            final Object msg
    ) throws Exception {
        if (msg instanceof HttpRequest) {
            if (this._reqHead == null) {
                this._reqHead = new NettyHttpRequestHead((HttpRequest) msg);
            }
        }

        if (msg instanceof HttpContent) {
            ByteBuf bodyPart = ((HttpContent) msg).content();
            if (bodyPart.readableBytes() == 0) {
                // empty body, do nothing
            } else if (this._bodySize + bodyPart.readableBytes() <= this._reqBodyBufSize.toByteVolume()) {
                this._bodyBuffer.add(bodyPart);
                this._bodySize += bodyPart.readableBytes();
            } else {
                NettyHttpRequestBody body = new NettyHttpRequestBodyFragment(
                        this._bodyBuffer.toArray(new ByteBuf[this._bodyBuffer.size()]));
                NettyHttpRequest request = new NettyHttpRequest(this._reqHead, body);
                NettyHttpResponse response = new NettyHttpResponse(channelCtx, this._reqHead);
                HttpEvent httpEvent = new HttpEvent(this._eventSource, request, response);
                this._eventBus.fire(httpEvent);
                this._fragmentBody = true;

                this._bodyBuffer.clear();
                this._bodyBuffer.add(bodyPart);
                this._bodySize = bodyPart.readableBytes();
            }

            if (msg instanceof LastHttpContent) {
                NettyHttpRequestBody body;
                if (this._fragmentBody) {
                    body = new NettyHttpRequestBodyFragment(
                            this._bodyBuffer.toArray(new ByteBuf[this._bodyBuffer.size()]));
                } else {
                    body = new NettyHttpRequestBody(
                            this._bodyBuffer.toArray(new ByteBuf[this._bodyBuffer.size()]));
                }
                NettyHttpRequest request = new NettyHttpRequest(this._reqHead, body);
                NettyHttpResponse response = new NettyHttpResponse(channelCtx, this._reqHead);
                HttpEvent httpEvent = new HttpEvent(this._eventSource, request, response);
                this._eventBus.fire(httpEvent, (event) -> event.response().close());
                this._bodyBuffer.clear();
                this._bodySize = 0;
            }
        }
    }

    @Override
    public void channelInactive(
            final ChannelHandlerContext ctx
    ) {
        ctx.close();
    }

    @Override
    public void exceptionCaught(
            final ChannelHandlerContext ctx,
            final Throwable cause
    ) {
        HttpException ex;
        if (cause instanceof HttpException) {
            ex = (HttpException) cause;
        } else {
            ex = HttpException.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).cause(cause).build();
        }
        outputError(ctx, ex);

        ctx.close();
    }

    private void outputError(
            final ChannelHandlerContext ctx,
            final HttpException ex
    ) {
        HttpResponseStatus status;
        switch (ex.status()) {
            case BAD_REQUEST:
                status = HttpResponseStatus.BAD_REQUEST;
                break;
            case NOT_FOUND:
                status = HttpResponseStatus.NOT_FOUND;
                break;
            default:
                status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        FullHttpMessage response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(ex.toString(), CharsetUtil.UTF_8));
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response);
        ctx.close();
    }
}
