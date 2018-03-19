package uapi.net.http.internal;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import uapi.GeneralException;
import uapi.net.INetListener;
import uapi.net.NetException;
import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;
import uapi.net.http.HttpAttributes;

@NetListener(type = HttpAttributes.TYPE)
public class HttpListener implements INetListener {

    @Attribute(value = HttpAttributes.HOST, isRequired = true)
    protected String _host;

    @Attribute(value = HttpAttributes.PORT, isRequired = true)
    protected int _port;

    private EventLoopGroup _bossGroup;
    private EventLoopGroup _workerGroup;

    private ChannelFuture _channelFuture;

    @Override
    public void startUp() throws NetException {
        this._bossGroup = new NioEventLoopGroup();
        this._workerGroup = new NioEventLoopGroup();
        ServerBootstrap svcBootstrap = new ServerBootstrap();
        svcBootstrap.group(this._bossGroup, this._workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new HttpResponseEncoder());
                        channel.pipeline().addLast(new HttpRequestDecoder());
//                        channel.pipeline().addLast(new HttpS)
                    }
                }).option(ChannelOption.SO_BACKLOG, 28)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            this._channelFuture = svcBootstrap.bind(this._host, this._port).sync();
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    @Override
    public void shutDown() throws NetException {
        if (this._workerGroup != null) {
            this._workerGroup.shutdownGracefully();
        }
        if (this._bossGroup != null) {
            this._bossGroup.shutdownGracefully();
        }

        try {
            if (this._channelFuture != null) {
                this._channelFuture.channel().closeFuture().sync();
            }
        } catch (InterruptedException ex) {
            // do nothing
        }
    }

    private class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        }
    }
}
