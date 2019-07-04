package top.zkq.douyu.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import top.zkq.douyu.client.entity.DyMap;
import top.zkq.douyu.client.entity.DyMessage;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/7/4 15:58
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            DyMap d = new DyMap();
            d.put("type", "keeplive");
            d.put("tick", String.valueOf(System.currentTimeMillis()));
            ctx.channel().writeAndFlush(new DyMessage(DyMessage.REQUEST, d));
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
