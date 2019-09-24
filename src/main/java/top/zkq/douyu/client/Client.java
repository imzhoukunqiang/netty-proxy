package top.zkq.douyu.client;

import com.google.common.base.Charsets;
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
import top.zkq.Commons;
import top.zkq.douyu.client.codec.DyMessageDecoder;
import top.zkq.douyu.client.handler.FirstConnectedHandler;
import top.zkq.douyu.client.handler.HeartbeatHandler;
import top.zkq.douyu.client.handler.MsgLogHandler;
import top.zkq.douyu.client.handler.PrintMessageHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 15:01
 */
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private Bootstrap bootstrap;
    private Channel channel;
    private NioEventLoopGroup group;
    private String roomId;
    private BufferedWriter writer;
    private volatile boolean closed = false;

    public Client(String roomId) throws IOException {
        this.roomId = roomId;
        group = new NioEventLoopGroup(1);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("弹幕.txt", true), Charsets.UTF_8);
        writer = new BufferedWriter(out, 128);
        bootstrap = new Bootstrap().group(group)
                                   .channel(NioSocketChannel.class)
                                   .remoteAddress(new InetSocketAddress("openbarrage.douyutv.com", 8601))
                                   .handler(new ChannelInitializer<Channel>() {
                                       @Override
                                       protected void initChannel(Channel ch) throws Exception {
                                           ChannelPipeline pipeline = ch.pipeline();
                                           pipeline.addLast(new IdleStateHandler(0, 30, 0));
                                           pipeline.addLast(new DyMessageDecoder());
                                           pipeline.addLast(new MsgLogHandler());
                                           pipeline.addLast(new PrintMessageHandler(writer));
                                           pipeline.addLast(new HeartbeatHandler());
                                           pipeline.addLast(new FirstConnectedHandler(Client.this.roomId));
                                           LOGGER.info("connected to DouYu.");
                                       }
                                   });
    }

    public static void main(String[] args) throws IOException {
        String roomId = args.length > 0 ? args[0] : "9999";
        Client client = new Client(roomId);
        client.start();

    }

    public void start() {
        doConnect(0);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("正在退出....");
            Client.this.close();
            LOGGER.info("退出....");
        }));
    }

    public void close() {
        closed = true;
        try {
            channel.close().sync();
            writer.flush();
            Commons.close(writer);
        } catch (Exception e) {

        }
        group.shutdownGracefully();
    }

    private void doConnect(long delay) {
        if (channel != null && channel.isActive() && !closed) {
            return;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException ignored) {
            //不会发生
        }
        LOGGER.info("try connect.");
        ChannelFuture connect = bootstrap.connect();
        connect.addListener(f -> {
            if (!f.isSuccess()) {
                doConnect(1000);
            } else {
                Channel channel = connect.channel();
                updateChannel(channel);
                channel.closeFuture().addListener(f1 -> {
                    doConnect(1000);
                });
            }
        });

    }

    private void updateChannel(Channel channel) {
        this.channel = channel;
    }

}
