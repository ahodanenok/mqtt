package ahodanenok.mqtt.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import ahodanenok.mqtt.server.packet.decoder.PacketDecoders;
import ahodanenok.mqtt.server.packet.encoder.PacketEncoders;

public final class MqttServer {

    public static void main(String... args) throws Exception {
        new MqttServer().run();
    }

    public void run() throws Exception {
        PacketDecoders packetDecoders = new PacketDecoders();
        PacketEncoders packetEncoders = new PacketEncoders();

        NioEventLoopGroup managerGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(managerGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(
                            new MqttServerHandler(packetDecoders, packetEncoders));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = boot.bind(8095).sync();
            f.channel().closeFuture().sync();
        } finally {
            managerGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
