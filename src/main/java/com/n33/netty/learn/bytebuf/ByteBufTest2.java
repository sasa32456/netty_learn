package com.n33.netty.learn.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;

/**
 * @author N33
 * @date 2019/8/18
 */
public class ByteBufTest2 {
    public static void main(String[] args) {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();

        ByteBuf heapBuf = Unpooled.buffer(10);
        ByteBuf directBuf = Unpooled.directBuffer(8);

        compositeByteBuf.addComponents(heapBuf, directBuf);

//        compositeByteBuf.removeComponent(0);

        Iterator<ByteBuf> iter = compositeByteBuf.iterator();

        //UnpooledSlicedByteBuf(ridx: 0, widx: 0, cap: 0/0, unwrapped: UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0, widx: 0, cap: 8))
//        while (iter.hasNext()) {
//            System.out.println(iter.next());
//        }

        //UnpooledSlicedByteBuf(ridx: 0, widx: 0, cap: 0/0, unwrapped: UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 0, cap: 10))
        //UnpooledSlicedByteBuf(ridx: 0, widx: 0, cap: 0/0, unwrapped: UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0, widx: 0, cap: 8))
        compositeByteBuf.forEach(System.out::println);

    }
}
