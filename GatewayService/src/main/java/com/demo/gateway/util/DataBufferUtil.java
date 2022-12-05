package com.demo.gateway.util;

import io.netty.buffer.ByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

import java.nio.charset.StandardCharsets;

public class DataBufferUtil {

    public static DataBuffer stringToDataBuffer(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT).allocateBuffer(bytes.length);
        buffer.write(buffer);
        return buffer;
    }

    public static String dataBufferToString(DataBuffer buffer) {
        byte[] bytes = new byte[buffer.readableByteCount()];
        buffer.read(bytes);
        DataBufferUtils.release(buffer);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
