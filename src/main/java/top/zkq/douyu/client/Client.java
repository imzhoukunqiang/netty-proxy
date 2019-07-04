package top.zkq.douyu.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zkq.douyu.client.codec.DyMessageDecoder;
import top.zkq.douyu.client.handler.FristConnectedHandler;
import top.zkq.douyu.client.handler.HeartbeatHandler;
import top.zkq.douyu.client.handler.MsgLogHandler;
import top.zkq.douyu.client.handler.PrintMessageHandler;

import java.net.InetSocketAddress;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 15:01
 */
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        String roomId = "9999";
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Channel channel = null;
        try {
            ChannelFuture future = bootstrap.group(group)
                                            .channel(NioSocketChannel.class)
                                            .remoteAddress(new InetSocketAddress("openbarrage.douyutv.com", 8601))
                                            .handler(new ChannelInitializer<Channel>() {
                                                @Override
                                                protected void initChannel(Channel ch) throws Exception {
                                                    ChannelPipeline pipeline = ch.pipeline();
                                                    pipeline.addLast(new IdleStateHandler(0, 30, 0));
                                                    pipeline.addLast(new DyMessageDecoder());
                                                    pipeline.addLast(new MsgLogHandler());
                                                    pipeline.addLast(new PrintMessageHandler());
                                                    pipeline.addLast(new HeartbeatHandler());
                                                    pipeline.addLast(new FristConnectedHandler(roomId));
                                                    LOGGER.info("connected to DouYu.");
                                                }
                                            })
                                            .connect();
            channel = future.sync().channel();

        } finally {
            if (channel != null) {
                channel.closeFuture().addListener(f -> {
                    group.shutdownGracefully();
                });
            } else {
                group.shutdownGracefully();
            }
        }

    }


}
