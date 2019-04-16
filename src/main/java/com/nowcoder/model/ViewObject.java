package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;


/**
 * ViewObject是用来传递对象和Velocity模板中间的对象
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
