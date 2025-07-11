package com.luminia.bridge.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.experimental.Delegate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

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

    public UUID readUuid() {
        return new UUID(buffer.readLongLE(), buffer.readLongLE());
    }

    public void writeUuid(UUID uuid) {
        buffer.writeLongLE(uuid.getMostSignificantBits());
        buffer.writeLongLE(uuid.getLeastSignificantBits());
    }

    public <T> void readArray(Collection<T> array, Supplier<T> supplier) {
        this.readArray(array, () -> ByteBufVarInts.readUnsignedInt(buffer), supplier);
    }

    public <T> void readArray(Collection<T> array, LongSupplier lengthReader, Supplier<T> supplier) {
        long length = lengthReader.getAsLong();
        for (int i = 0; i < length; i++) {
            array.add(supplier.get());
        }
    }

    public <T> void writeArray(T[] array, Consumer<T> consumer) {
        ByteBufVarInts.writeUnsignedInt(buffer, array.length);
        for (T val : array) {
            consumer.accept(val);
        }
    }

    public <T> void writeArray(Collection<T> array, Consumer<T> consumer) {
        this.writeArray(array, size -> ByteBufVarInts.writeUnsignedInt(buffer, size), consumer);
    }

    public <T> void writeArray(Collection<T> array, IntConsumer lengthWriter, Consumer<T> consumer) {
        lengthWriter.accept(array.size());
        for (T val : array) {
            consumer.accept(val);
        }
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
