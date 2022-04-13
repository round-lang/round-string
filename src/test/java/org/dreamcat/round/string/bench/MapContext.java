package org.dreamcat.round.string.bench;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.context.Context;

/**
 * @author Jerry Will
 * @version 2022-03-19
 */
@RequiredArgsConstructor
public class MapContext implements Context {

    public final Map<String, Object> map;

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public String[] getKeys() {
        return map.keySet().toArray(new String[0]);
    }

    @Override
    public Object remove(String key) {
        return map.remove(key);
    }
}
