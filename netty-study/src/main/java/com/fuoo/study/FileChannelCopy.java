package com.fuoo.study;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelCopy {
    public static void main(String[] args) throws Exception{
        FileOutputStream out = new FileOutputStream("F:/fileChannel2.txt");
        FileInputStream in = new FileInputStream("F:/fileChannel.txt");
        FileChannel outChannel = out.getChannel();
        FileChannel inChannel = in.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        inChannel.read(byteBuffer);

        byteBuffer.flip();

        outChannel.write(byteBuffer);

        out.close();
        in.close();
    }
}
