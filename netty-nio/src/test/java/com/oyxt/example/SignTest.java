package com.oyxt.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author 20190712713
 * @date 2019/12/30 14:12
 */
public class SignTest {
    public static void main(String[] args) {
        //40
        byte[] bytes1 = new byte[] {(byte)0x00,(byte)0x28};
        //-40
        byte[] bytes2 = new byte[] {(byte)0xFF,(byte)0xD8};
        System.out.println("------------");
        ByteBuf in = Unpooled.copiedBuffer(bytes2);

        if(in.readableBytes() >= 2) {
            System.out.println("--------------------------------------------------");

            int temp = in.readUnsignedShort();
            System.out.println(temp);
//            System.out.println(in.readShort());
        }
    }
}
