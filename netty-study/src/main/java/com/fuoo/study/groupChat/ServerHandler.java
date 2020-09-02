package com.fuoo.study.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @author fuoo
 * @title ServerHandler
 * @projectName netty-study
 * @description 入站处理处理
 * @date 2020/9/216:36
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    DefaultChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        /*
            该方法会将 channelGroup 中所有的 channel 遍历，并发送 消息，
            我们不需要自己遍历
        */
        channelGroup.writeAndFlush("[ 客 户 端 ]" + channel.remoteAddress() + " 加 入 聊 天 " + sdf.format(new
                java.util.Date()) + " \n");
        channelGroup.add(channel);

        System.out.println("handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        /*
            该方法会将 channelGroup 中所有的 channel 遍历，并发送 消息，
            我们不需要自己遍历
        */
        channelGroup.writeAndFlush("[ 客 户 端 ]" + channel.remoteAddress() + " 加入离开 " + sdf.format(new
                java.util.Date()) + " \n");
        channelGroup.remove(channel);

        System.out.println("handlerRemoved");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        //获取到当前 channel
        Channel channel = channelHandlerContext.channel();
        //这时我们遍历 channelGroup, 根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if(channel != ch) { //不是当前的 channel,转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + s + "\n");
            }else {//回显自己发送的消息给自己
                ch.writeAndFlush("[自己]发送了消息" + s + "\n");
            }
        });

    }

    //表示 channel 处于活动状态, 提示 xx 上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
    }
    //表示 channel 处于不活动状态, 提示 xx 离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
