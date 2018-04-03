/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.net.http.netty.internal;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import uapi.GeneralException;
import uapi.net.INetListener;
import uapi.net.NetException;
import uapi.net.annotation.Attribute;
import uapi.net.annotation.NetListener;
import uapi.net.http.HttpAttributes;
import uapi.state.IChecker;
import uapi.state.IShifter;
import uapi.state.IStateTracer;
import uapi.state.StateCreator;

@NetListener(type = HttpAttributes.TYPE)
public class NettyHttpListener implements INetListener {

    private static final String OP_START_UP         = "startUp";
    private static final String OP_SHUT_DOWN        = "shutDown";

    @Attribute(value = HttpAttributes.HOST, isRequired = true)
    protected String _host;

    @Attribute(value = HttpAttributes.PORT, isRequired = true)
    protected int _port;

    private EventLoopGroup _bossGroup;
    private EventLoopGroup _workerGroup;

    private ChannelFuture _channelFuture;

    private final IStateTracer<ListenerState> _stateTracer;

    public NettyHttpListener() {
        // Create state transition rule
        IChecker<ListenerState> stateChecker = (currentState, operation) -> {
            ListenerState temporaryState = null;
            switch (operation.type()) {
                case OP_START_UP:
                    if (currentState.value() == ListenerState.Stopped.value()) {
                        temporaryState = ListenerState.Starting;
                    }
                    break;
                case OP_SHUT_DOWN:
                    if (currentState == ListenerState.Started) {
                        temporaryState = ListenerState.Stopping;
                    }
                    break;
                default:
                    throw new GeneralException();
            }
            return temporaryState;
        };
        IShifter<ListenerState> stateShifter = (currentState, operation) -> {
            ListenerState newState;
            switch(operation.type()) {
                case OP_START_UP:
                    innerStartUp();
                    newState = ListenerState.Started;
                    break;
                case OP_SHUT_DOWN:
                    innerShutDown();
                    newState = ListenerState.Stopped;
                    break;
                default:
                    throw new GeneralException();
            }
            return newState;
        };
        this._stateTracer = StateCreator.createTracer(stateShifter, ListenerState.Stopped, stateChecker);
    }

    @Override
    public void startUp() {
        this._stateTracer.shift(OP_START_UP);
    }

    @Override
    public void shutDown() {
        this._stateTracer.shift(OP_SHUT_DOWN);
    }

    private void innerStartUp() throws NetException {
        this._bossGroup = new NioEventLoopGroup();
        this._workerGroup = new NioEventLoopGroup();
        ServerBootstrap svcBootstrap = new ServerBootstrap();
        svcBootstrap.group(this._bossGroup, this._workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new HttpServerCodec());
                        channel.pipeline().addLast(new HttpRequestHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 28)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            this._channelFuture = svcBootstrap.bind(this._host, this._port).sync();
        } catch (InterruptedException ex) {
            innerShutDown();
            throw NetException.builder().cause(ex).build();
        }
    }

    private void innerShutDown() throws NetException {
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
}
