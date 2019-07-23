package top.zkq.douyu.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zkq.douyu.client.entity.DyData;
import top.zkq.douyu.client.entity.DyMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 15:18
 */
@ChannelHandler.Sharable
public class PrintMessageHandler extends SimpleChannelInboundHandler<DyMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintMessageHandler.class);
    public static final String TYPE = "type";

    public Writer writer;

    public PrintMessageHandler() throws IOException {
        writer = new BufferedWriter(new FileWriter("D:\\data\\弹幕.txt"));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DyMessage msg) throws Exception {
        DyData data = msg.getData();
        if ("chatmsg".equals(data.getString(TYPE))) {
            String uid = data.getString("uid");
            String nn = data.getString("nn");
            String level = data.getString("level");
            String txt = data.getString("txt");
            String bnn = data.getString("bnn");
            String format = MessageFormat.format("[{0} {1}] [lv.{2} {3}] {4}", uid, nn, level, bnn, txt);
            writer.write(format);
            writer.write('\n');
            LOGGER.info(format);
        } else {
            //    System.out.println(data.encode());
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.channelUnregistered(ctx);
    }
}
