package net.wytrem.spigot.exmachina.refs;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class FromPath extends ScriptRef {
    private final String path;

    public FromPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", path)
                .toString();
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String, Object>() {{
            put("path", path);
        }};
    }
}
