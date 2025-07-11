package com.luminia.bridge.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.experimental.Delegate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class ByteBuffer {

    @Delegate
    private final ByteBuf buffer;

    private ByteBuffer(ByteBuf buffer) {
        this.buffer = Objects.requireNonNull(buffer);
    }

    public static ByteBuffer of(ByteBuf buffer) {
        return new ByteBuffer(buffer);
    }

    public String readString() {
        int length = this.readUnsignedVarInt();
        return (String) buffer.readCharSequence(length, StandardCharsets.UTF_8);
    }

    public void writeString(String string) {
        this.writeUnsignedVarInt(ByteBufUtil.utf8Bytes(string));
        buffer.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public void writeUnsignedVarInt(int value) {
        ByteBufVarInts.writeUnsignedInt(buffer, value);
    }

    public int readUnsignedVarInt() {
        return ByteBufVarInts.readUnsignedInt(buffer);
    }

    public void writeVarInt(int value) {
        ByteBufVarInts.writeInt(buffer, value);
    }

    public int readVarInt() {
        return ByteBufVarInts.readInt(buffer);
    }

    public void writeUnsignedVarLong(long value) {
        ByteBufVarInts.writeUnsignedLong(buffer, value);
    }

    public long readUnsignedVarLong() {
        return ByteBufVarInts.readUnsignedLong(buffer);
    }

    public void writeUnsignedBigVarInt(BigInteger value) {
        ByteBufVarInts.writeUnsignedBigVarInt(buffer, value);
    }

    public BigInteger readUnsignedBigVarInt(int length) {
        return ByteBufVarInts.readUnsignedBigVarInt(buffer, length);
    }

    public void writeVarLong(long value) {
        ByteBufVarInts.writeLong(buffer, value);
    }

    public long readVarLong() {
        return ByteBufVarInts.readLong(buffer);
    }
}
