package top.zkq.douyu.client.entity;

import java.nio.charset.StandardCharsets;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 10:39
 */
public class DyMessage {

    public static final short REQUEST = 689;
    public static final short RESPONSE = 690;

    private final short type;
    private final DyData data;

    public DyMessage(short type, DyData data) {
        this.type = type;
        this.data = data;
    }

    /**
     * 消息长度
     */
    public int length() {
        return 8 + body().length + 1;
    }

    /**
     * 消息类型
     */
    public short type() {
        return type;
    }


    /**
     * 消息体
     * 结尾为 \0
     */
    public byte[] body() {
        return data.encode().getBytes(StandardCharsets.UTF_8);
    }


    /**
     * 结束符号
     */
    public byte end() {
        return '\0';
    }

    /**
     * 加密字段
     */
    public byte encrypt() {
        return 0;
    }

    /**
     * 保留字段
     */
    public byte blank() {
        return 0;
    }

    public DyData getData() {
        return data;
    }
}
