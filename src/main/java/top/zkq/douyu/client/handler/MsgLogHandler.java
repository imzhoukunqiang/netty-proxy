package top.zkq.douyu.client.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zkq.douyu.client.entity.DyMessage;


/**
 * @author zkq
 * @version 1.0
 * @date 2019/6/10 10:55
 */
public class MsgLogHandler extends ChannelDuplexHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgLogHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DyMessage) {
            //打印read日志
            //LOGGER.info(((DyMessage) msg).getData().toString());
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof DyMessage) {
            //打印write日志
           // LOGGER.info(((DyMessage) msg).getData().getString("type"));
        }
        ctx.write(msg, promise);
    }
}
