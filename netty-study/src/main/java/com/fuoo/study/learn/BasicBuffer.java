package com.fuoo.study.learn;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);
        }
        System.out.println(intBuffer.toString());
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            intBuffer.mark();
            System.out.println(intBuffer.position());
            System.out.println(intBuffer.get());
        }
    }
}
