package ahodanenok.mqtt.server.netty;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import ahodanenok.mqtt.server.ClientConnection;
import ahodanenok.mqtt.server.MqttProtocol;
import ahodanenok.mqtt.server.packet.PacketLength;
import ahodanenok.mqtt.server.packet.decoder.DecodeUtils;
import ahodanenok.mqtt.server.packet.decoder.PacketDecoders;
import ahodanenok.mqtt.server.packet.encoder.PacketEncoders;

public class MqttServerHandler extends ChannelInboundHandlerAdapter {

    private final PacketDecoders packetDecoders;
    private final PacketEncoders packetEncoders;
    private MqttProtocol protocol;
    private PacketLength packetLength;
    // todo: adjust buffer size dynamically
    private ByteBuffer inputBuf = ByteBuffer.allocate(128);

    public MqttServerHandler(
            PacketDecoders packetDecoders,
            PacketEncoders packetEncoders) {
        this.packetDecoders = packetDecoders;
        this.packetEncoders = packetEncoders;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ClientConnection clientConnection =
            new ClientConnection(new NettyDataConnection(ctx), packetEncoders);
        protocol = new MqttProtocol(clientConnection);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        while (buf.readableBytes() > 0) {
            inputBuf.put(buf.readByte());
        }
        buf.release();

        if (packetLength == null) {
            int pos = inputBuf.position();
            inputBuf.position(1);
            packetLength = DecodeUtils.decodePacketLength(inputBuf);
            inputBuf.position(pos);
        }
        if (inputBuf.position() + packetLength.getBytes() + 2 < packetLength.getValue()) {
            return;
        }

        inputBuf.rewind();
        protocol.onPacket(packetDecoders.decode(inputBuf));
        // can't rewind - there may be some data from the next packet
        //inputBuf.rewind();
        // todo: don't allocate and copy each time, use two buffers?
        ByteBuffer tmp = ByteBuffer.allocate(128);
        while (inputBuf.hasRemaining()) {
            tmp.put(inputBuf.get());
        }
        tmp.rewind();
        inputBuf = tmp;

        packetLength = null;
    }
}
