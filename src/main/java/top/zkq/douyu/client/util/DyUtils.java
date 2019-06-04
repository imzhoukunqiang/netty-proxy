package top.zkq.douyu.client.util;

import top.zkq.Commons;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 11:50
 */
public final class DyUtils {
    private DyUtils() {
    }

    public static String encode(String s) {
        if (Commons.isEmpty(s)) {
            return s;
        }
        return s.replace("/", "@S").replace("@", "@A");
    }

    public static String decode(String s) {
        if (Commons.isEmpty(s)) {
            return s;
        }
        return s.replace("@S", "/").replace("@A", "@");
    }
}
