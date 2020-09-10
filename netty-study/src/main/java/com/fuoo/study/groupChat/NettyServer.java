package com.fuoo.study.groupChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author fuoo
 * @title NettyServer
 * @projectName netty-study
 * @description
 * @date 2020/9/215:53
 */
public class NettyServer {
    private int port; //监听端口尚硅谷 Netty 核心技术及源码剖析

    public NettyServer(int port) {
        this.port = port;
    }

    //编写 run 方法，处理客户端的请求
    public void run() throws Exception{
//创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //8 个 NioEventLoop
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到 pipeline尚硅谷 Netty 核心技术及源码剖析
                            ChannelPipeline pipeline = ch.pipeline();
                            //向 pipeline 加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //向 pipeline 加入编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自己的业务处理 handler
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = b.bind(port).sync();
//监听关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        new NettyServer(7000).run();
    }
}
