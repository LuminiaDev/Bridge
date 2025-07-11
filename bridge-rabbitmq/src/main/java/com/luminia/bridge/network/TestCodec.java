package com.luminia.bridge.network;

import com.luminia.bridge.network.codec.BridgeCodec;

public final class TestCodec {

    public static final BridgeCodec CODEC = BridgeCodec.builder()
            .registerPacket(TestPacket.IDENTIFIER, TestPacket::new, new TestPacket.TestPacketSerializer())
            .build();
}
