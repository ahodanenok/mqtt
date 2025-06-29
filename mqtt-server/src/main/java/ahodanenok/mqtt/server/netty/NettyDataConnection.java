package ahodanenok.mqtt.server.netty;

import java.nio.ByteBuffer;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import ahodanenok.mqtt.server.DataConnection;

public final class NettyDataConnection implements DataConnection {

    private final ChannelHandlerContext ctx;

    public NettyDataConnection(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void send(ByteBuffer buf) {
        ctx.writeAndFlush(io.netty.buffer.Unpooled.wrappedBuffer(buf));
    }

    @Override
    public void close() {
        ctx.close();
    }
}
