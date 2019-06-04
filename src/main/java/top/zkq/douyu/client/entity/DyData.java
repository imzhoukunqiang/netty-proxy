package top.zkq.douyu.client.entity;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 11:39
 */
public interface DyData {
    String encode();

    DyData getDyData(String key);

    Object put(String key, DyData value);

    Object put(String key, String value);

    String getString(String key);
}
