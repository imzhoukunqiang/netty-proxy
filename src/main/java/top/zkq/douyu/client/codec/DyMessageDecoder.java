package top.zkq.douyu.client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import top.zkq.douyu.client.entity.DyMap;
import top.zkq.douyu.client.entity.DyMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 14:15
 */
public class DyMessageDecoder extends ByteToMessageCodec<DyMessage> {
    boolean read = false;
    private int length = 0;
    public static final int HEAD = 4 + 2 + 1 + 1;

    public DyMessageDecoder() {
        super(false);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, DyMessage msg, ByteBuf out) throws Exception {
        out.writeIntLE(msg.length());
        out.writeIntLE(msg.length());
        out.writeShortLE(msg.type());
        out.writeByte(msg.encrypt());
        out.writeByte(msg.blank());
        out.writeBytes(msg.body());
        out.writeByte(msg.end());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 4 + 2 + 1 + 1;
        if (!read) {
            if (in.readableBytes() > 4) {
                length = in.readIntLE();
                read = true;
                decode(ctx, in, out);
            }
        } else {
            int i = in.readableBytes();
            if (i >= length) {
                in.skipBytes(HEAD);
                String s = in.readCharSequence(length - HEAD, StandardCharsets.UTF_8).toString();
                DyMap dyMap = new DyMap();
                dyMap.from(s);
                DyMessage message = new DyMessage(DyMessage.RESPONSE, dyMap);
                out.add(message);
                reset();
                decode(ctx, in, out);
            }
        }

    }

    private void reset() {
        read = false;
        length = 0;
    }
}
