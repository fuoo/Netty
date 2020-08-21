package com.fuoo.study.train;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient2 {
    private Selector selector;
    private SocketChannel socketChannel;
    private static final String host = "127.0.0.1";
    private static final int PORT = 6666;

    public static void main(String[] args) throws Exception {
        GroupChatClient2 groupChatClient = new GroupChatClient2();
        groupChatClient.lisent();
        groupChatClient.write();
    }

    public GroupChatClient2() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(host,PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            try {
                socketChannel.write(ByteBuffer.wrap(scanner.next().getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void lisent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        selector.select();
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            channel.read(byteBuffer);
                            System.out.println(new String(byteBuffer.array()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
