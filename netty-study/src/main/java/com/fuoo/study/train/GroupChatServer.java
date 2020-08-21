package com.fuoo.study.train;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 6666;

    public static void main(String[] args) throws Exception {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

    public GroupChatServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() throws Exception {
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isAcceptable()) {
                    SocketChannel accept = serverSocketChannel.accept();

                    System.out.println("用户" + accept.getRemoteAddress() + "上线!!!");
                    informUser(selectionKey, "用户" + accept.getRemoteAddress() + "上线!!!");

                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel =  (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int readSize = -1;
                    do {
                        readSize = socketChannel.read(byteBuffer);

                    } while (readSize > 0);
                    informUser(selectionKey, new String(byteBuffer.array()));
                }
            }
            iterator.remove();
        }
    }

    public void informUser(SelectionKey selectionKey, String msg) {
        Set<SelectionKey> keys = selector.keys();
        keys.forEach(key -> {
            if (key.channel() instanceof SocketChannel && !key.channel().equals(selectionKey.channel())) {
                SocketChannel channel = (SocketChannel)key.channel();
                try {
                    channel.write(ByteBuffer.wrap(("用户" + channel.getRemoteAddress() + "说：" + msg).getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
