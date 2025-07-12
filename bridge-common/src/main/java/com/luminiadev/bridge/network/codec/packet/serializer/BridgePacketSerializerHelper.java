package com.luminiadev.bridge.network.codec.packet.serializer;

import com.luminiadev.bridge.util.VarInts;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.ToLongFunction;

@AllArgsConstructor
public final class BridgePacketSerializerHelper {

    private final ByteBuf buffer;

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

    public <T> T[] readArray(T[] array, Function<ByteBuf, T> function) {
        List<T> list = new ArrayList<>();
        readArray(list, function);
        return list.toArray(array);
    }

    public <T> void readArray(Collection<T> array, Function<ByteBuf, T> function) {
        this.readArray(array, VarInts::readUnsignedInt, function);
    }

    public <T> void readArray(Collection<T> array, ToLongFunction<ByteBuf> lengthReader, Function<ByteBuf, T> function) {
        long length = lengthReader.applyAsLong(buffer);
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer));
        }
    }

    public <T> void writeArray(Collection<T> array, BiConsumer<ByteBuf, T> consumer) {
        this.writeArray(array, VarInts::writeUnsignedInt, consumer);
    }

    public <T> void writeArray(Collection<T> array, ObjIntConsumer<ByteBuf> lengthWriter, BiConsumer<ByteBuf, T> consumer) {
        lengthWriter.accept(buffer, array.size());
        for (T val : array) {
            consumer.accept(buffer, val);
        }
    }

    public <T> void writeArray(T[] array, BiConsumer<ByteBuf, T> consumer) {
        VarInts.writeUnsignedInt(buffer, array.length);
        for (T val : array) {
            consumer.accept(buffer, val);
        }
    }

    public void writeUnsignedVarInt(int value) {
        VarInts.writeUnsignedInt(buffer, value);
    }

    public int readUnsignedVarInt() {
        return VarInts.readUnsignedInt(buffer);
    }

    public void writeVarInt(int value) {
        VarInts.writeInt(buffer, value);
    }

    public int readVarInt() {
        return VarInts.readInt(buffer);
    }

    public void writeUnsignedVarLong(long value) {
        VarInts.writeUnsignedLong(buffer, value);
    }

    public long readUnsignedVarLong() {
        return VarInts.readUnsignedLong(buffer);
    }

    public void writeUnsignedBigVarInt(BigInteger value) {
        VarInts.writeUnsignedBigVarInt(buffer, value);
    }

    public BigInteger readUnsignedBigVarInt(int length) {
        return VarInts.readUnsignedBigVarInt(buffer, length);
    }

    public void writeVarLong(long value) {
        VarInts.writeLong(buffer, value);
    }

    public long readVarLong() {
        return VarInts.readLong(buffer);
    }
}