package com.fuoo.study.groupChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * @author fuoo
 * @title NettyClient
 * @projectName netty-study
 * @description
 * @date 2020/9/216:58
 */
public class NettyClient {
    private int port;
    private String ip;

    public NettyClient(int port, String ip) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<SocketChannel>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SocketChannel socketChannel) throws Exception {
                        //得到 pipeline
                        ChannelPipeline pipeline = channelHandlerContext.pipeline();
                        //加入相关 handler

                        //加入自定义的 handler
                        pipeline.addLast(null);

                    }
                });

            try {
                ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
                //得到 channel
                Channel channel = channelFuture.channel();
                System.out.println("-------" + channel.localAddress()+ "--------");
                //客户端需要输入信息，创建一个扫描器
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String msg = scanner.nextLine();
                    //通过 channel 发送到服务器端
                    channel.writeAndFlush(msg + "\r\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                loopGroup.shutdownGracefully();
            }
    }


    public static void main(String[] args) throws Exception {
        new NettyClient(55551, "127.0.0.1").run();
    }
}
