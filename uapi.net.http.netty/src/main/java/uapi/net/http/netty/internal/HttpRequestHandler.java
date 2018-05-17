package uapi.net.http.netty.internal;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import uapi.event.IEventBus;
import uapi.net.http.HttpEvent;
import uapi.net.http.HttpException;
import uapi.net.http.HttpStatus;

class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private final IEventBus _eventBus;
    private final String _eventSource;

    private NettyHttpRequest _request;
    private NettyHttpResponse _response;

    HttpRequestHandler(
            final IEventBus eventBus,
            final String eventSource
    ) {
        this._eventBus = eventBus;
        this._eventSource = eventSource;
    }

    @Override
    public void channelRead(
            final ChannelHandlerContext channelCtx,
            final Object msg
    ) throws Exception {
        if (msg instanceof HttpRequest) {
            if (this._request == null) {
                this._request = new NettyHttpRequest((HttpRequest) msg);
            }
            if (this._response == null) {
                this._response = new NettyHttpResponse(channelCtx, this._request);
            }
        }

        if (msg instanceof HttpContent) {
            this._request.appendBody((HttpContent) msg);
            HttpEvent httpEvent = new HttpEvent(this._eventSource, this._request, this._response);
            this._eventBus.fire(httpEvent, (event) -> event.response().close());
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
