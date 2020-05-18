package net.wytrem.spigot.exmachina.refs;

import com.google.common.base.MoreObjects;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * References a "inlined" {@link net.wytrem.spigot.exmachina.Script}: stores its source code.
 */
@SerializableAs("exmachina:inline")
public class InlineImpl implements ScriptRef {

    /**
     * The script source code.
     */
    private final String source;

    public InlineImpl(String source) {
        this.source = source;
    }

    /**
     * @return the script source code
     */
    public String getSource() {
        return source;
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String, Object>() {{
            put("source", source);
        }};
    }

    public static InlineImpl valueOf(Map<String, Object> map) {
        return new InlineImpl(map.get("source").toString());
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
        InlineImpl inlineImpl = (InlineImpl) o;
        return Objects.equals(source, inlineImpl.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }
}
