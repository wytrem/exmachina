package net.wytrem.spigot.exmachina.refs;

import com.google.common.base.MoreObjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Inline extends ScriptRef {
    private final String source;

    public Inline(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String, Object>() {{
            put("source", source);
        }};
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("source", source)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inline inline = (Inline) o;
        return Objects.equals(source, inline.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }
}
