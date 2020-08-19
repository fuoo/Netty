package com.fuoo.study;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelRead {
    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream("F:/fileChannel.txt");
        FileChannel channel = in.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = channel.read(byteBuffer);
        System.out.println("读取文件内容: " + new String(byteBuffer.array(), 0, read));
        in.close();
    }
}
