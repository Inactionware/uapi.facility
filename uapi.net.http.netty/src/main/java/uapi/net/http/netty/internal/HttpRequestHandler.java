package uapi.net.http.netty.internal;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import uapi.net.http.HttpException;

class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private NettyHttpRequest _request;
    private NettyHttpResponse _response;

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
    }

    @Override
    public void channelReadComplete(
            final ChannelHandlerContext ctx
    ) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(
            final ChannelHandlerContext ctx,
            final Throwable cause
    ) {
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
    }
}
