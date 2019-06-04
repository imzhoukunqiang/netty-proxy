package top.zkq.douyu.client.entity;

import top.zkq.douyu.client.util.DyUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/23 11:39
 */
public class DyMap implements DyData {
    private HashMap<String, Object> data = new HashMap<>();
    private static final DyMap EMPTY = new DyMap();

    @Override
    public Object put(String key, DyData value) {
        return data.put(key, value);
    }

    @Override
    public Object put(String key, String value) {
        return data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public DyData getDyData(String key) {
        Object o = data.get(key);
        if (o == null) {
            return EMPTY;
        }
        if (o instanceof DyData) {
            return (DyData) o;
        } else {
            DyMap r = new DyMap();
            r.from((String) o);
            return r;
        }
    }

    @Override
    public String getString(String key) {
        Object o = data.get(key);
        if (o == null) {
            return "";
        }
        if (o instanceof String) {
            return (String) o;
        } else {
            return ((DyData) o).encode();
        }
    }

    @Override
    public String encode() {
        StringBuilder s = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            s.append(DyUtils.encode(entry.getKey())).append("@=");
            Object value = entry.getValue();
            if (value instanceof DyData) {
                s.append(((DyData) value).encode());
            } else {
                s.append(DyUtils.encode((String) value));
            }
            s.append("/");
        }
        return s.toString();
    }

    public void from(String s) {
        data.clear();
        String[] messages = s.split("/");
        Map<String, Object> m = data;
        for (String message : messages) {
            String[] ms = message.split("@=");
            if (ms.length == 2) {
                m.put(ms[0], ms[1]);
            }
        }
    }
}
