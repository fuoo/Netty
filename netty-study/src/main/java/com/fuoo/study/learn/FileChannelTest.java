package com.fuoo.study.learn;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    public static void main(String[] args) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream("F:/fileChannel.txt");

        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("我这学习fileChannel".getBytes());

        byteBuffer.flip();
        channel.write(byteBuffer);

        fileOutputStream.close();
    }

}
