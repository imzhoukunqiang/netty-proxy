package top.zkq.douyu.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zkq.douyu.client.entity.DyMap;
import top.zkq.douyu.client.entity.DyMessage;

import java.util.concurrent.TimeUnit;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/7/4 16:15
 */
public class FirstConnectedHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstConnectedHandler.class);
    private final String roomId;

    public FirstConnectedHandler(String roomId) {
        this.roomId = roomId;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        DyMap map = new DyMap();
        map.put("type", "loginreq");
        map.put("roomid", roomId);
        channel.writeAndFlush(new DyMessage(DyMessage.REQUEST, map)).sync();

        channel.eventLoop().schedule(() -> {
            DyMap m = new DyMap();
            m.put("type", "joingroup");
            m.put("rid", roomId);
            m.put("gid", "-9999");
            channel.writeAndFlush(new DyMessage(DyMessage.REQUEST, m));
            LOGGER.info("join " + roomId);
        }, 1000, TimeUnit.MILLISECONDS);
        channel.pipeline().remove(this);
        super.channelActive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("removed");
        super.handlerRemoved(ctx);
    }
}
