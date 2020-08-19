package com.fuoo.study;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(10000) == 0) {
                System.out.println("等待连接中......");
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("有客户端注册" + socketChannel.getRemoteAddress().toString());
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    byteBuffer.put("1234".getBytes());
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                }

                selectionKeys.remove(selectionKey);
            }

        }
    }
}
