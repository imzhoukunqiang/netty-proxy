package top.zkq.douyu.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zkq.douyu.client.entity.DyData;
import top.zkq.douyu.client.entity.DyMessage;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 15:18
 */
@ChannelHandler.Sharable
public class PrintMessageHandler extends SimpleChannelInboundHandler<DyMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintMessageHandler.class);
    public static final String TYPE = "type";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DyMessage msg) throws Exception {
        DyData data = msg.getData();
        if ("chatmsg".equals(data.getString(TYPE))) {
            String uid = data.getString("uid");
            String nn = data.getString("nn");
            String level = data.getString("level");
            String txt = data.getString("txt");
            String bnn = data.getString("bnn");
            LOGGER.info("[{} {}] [lv.{} {}] {}", uid, nn, level, bnn, txt);
        } else {
            //    System.out.println(data.encode());
        }
    }
}
