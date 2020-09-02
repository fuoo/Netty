package com.fuoo.study.groupChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author fuoo
 * @title NettyServer
 * @projectName netty-study
 * @description
 * @date 2020/9/215:53
 */
public class NettyServer {
    private int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup boosEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();

        serverBootstrap.group(boosEventLoopGroup, workEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ServerHandler());
                    }
                });

        try {

            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            //服务器同步连接断开时,这句代码才会往下执行
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosEventLoopGroup.shutdownGracefully();
            workEventLoopGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer(55551);
        nettyServer.run();
    }

}
