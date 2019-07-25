package com.n33.test;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

public class Test {
    public static void main(String[] args) {


        final int result = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));

        /**
         * 16 4核心，超线程技术=8核心
         */
        System.out.println(result);
    }
}
