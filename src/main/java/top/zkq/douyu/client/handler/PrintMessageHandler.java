package top.zkq.douyu.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.zkq.douyu.client.entity.DyData;
import top.zkq.douyu.client.entity.DyMessage;

import java.text.MessageFormat;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 15:18
 */
@ChannelHandler.Sharable
public class PrintMessageHandler extends SimpleChannelInboundHandler<DyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DyMessage msg) throws Exception {
        DyData data = msg.getData();
        if ("chatmsg".equals(data.getString("type"))) {
            String uid = data.getString("uid");
            String nn = data.getString("nn");
            String level = data.getString("level");
            String txt = data.getString("txt");
            String bnn  = data.getString("bnn");
            System.out.println(MessageFormat.format("[{0} {1}] [lv.{2} {4}] {3}", uid, nn, level, txt,bnn));
        } else {
        //    System.out.println(data.encode());
        }
    }
}
