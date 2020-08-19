package com.fuoo.study;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception{
        RandomAccessFile rw = new RandomAccessFile("F:/fileChannel.txt", "rw");
        FileChannel channel = rw.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(1,(byte)'H');
        map.put(2,(byte)'H');
        map.put(3,(byte)'H');

        rw.close();
    }
}
