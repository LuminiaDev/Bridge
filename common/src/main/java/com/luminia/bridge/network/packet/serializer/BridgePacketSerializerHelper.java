package com.luminia.bridge.network.packet.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class BridgePacketSerializerHelper {

    public void writeString(ByteBuf buffer, String string) {
        if (string == null) {
            buffer.writeInt(-1);
            return;
        }
        byte[] bytes = string.getBytes(CharsetUtil.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public String readString(ByteBuf buffer) {
        int length = buffer.readInt();
        if (length == -1) {
            return null;
        }
        if (length < 0 || length > buffer.readableBytes()) {
            throw new IllegalArgumentException("Invalid string length: " + length);
        }
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, CharsetUtil.UTF_8);
    }
}
