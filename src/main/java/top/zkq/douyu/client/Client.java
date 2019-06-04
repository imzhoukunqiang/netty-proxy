package top.zkq.douyu.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zkq.douyu.client.codec.DyMessageDecoder;
import top.zkq.douyu.client.entity.DyMap;
import top.zkq.douyu.client.entity.DyMessage;
import top.zkq.douyu.client.handler.PrintMessageHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

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

        try {
            ChannelFuture future = bootstrap.group(group)
                                            .channel(NioSocketChannel.class)
                                            .remoteAddress(new InetSocketAddress("openbarrage.douyutv.com", 8601))
                                            .handler(new ChannelInitializer<Channel>() {
                                                @Override
                                                protected void initChannel(Channel ch) throws Exception {
                                                    ChannelPipeline pipeline = ch.pipeline();
                                                    pipeline.addLast(new DyMessageDecoder());
                                                    pipeline.addLast(new PrintMessageHandler());
                                                    LOGGER.info("connected to DouYu.");
                                                }
                                            })
                                            .connect();
            Channel channel = future.sync().channel();
            DyMap map = new DyMap();
            map.put("type", "loginreq");
            map.put("roomid", roomId);
            channel.writeAndFlush(new DyMessage(DyMessage.REQUEST, map)).sync();

            ScheduledFuture<?> schedule = group.scheduleWithFixedDelay(() -> {
                DyMap d = new DyMap();
                d.put("type", "keeplive");
                d.put("tick", String.valueOf(System.currentTimeMillis()));
                channel.writeAndFlush(new DyMessage(DyMessage.REQUEST, d));
            }, 0, 30, TimeUnit.SECONDS);

            Thread.sleep(1000);
            map = new DyMap();
            map.put("type", "joingroup");
            map.put("rid", roomId);
            map.put("gid", "-9999");
            channel.writeAndFlush(new DyMessage(DyMessage.REQUEST, map));


            channel.closeFuture().addListener(f -> {
                schedule.cancel(true);
            }).sync();
        } finally {
            group.shutdownGracefully().sync();
        }

    }


}
