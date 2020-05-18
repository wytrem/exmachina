package net.wytrem.spigot.exmachina.refs;

import com.google.common.base.MoreObjects;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * References a {@link net.wytrem.spigot.exmachina.Script} stored in a dedicated file inside the scripts/ folder.
 */
@SerializableAs("exmachina:file")
public class FromFile implements ScriptRef {

    /**
     * The path, relative to the scripts/ folder, without extension.
     */
    private final String path;

    public FromFile(String path) {
        this.path = path;
    }

    /**
     * @return The path, relative to the scripts/ folder, without extension
     */
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

    public static FromFile valueOf(Map<String, Object> map) {
        return new FromFile(map.get("path").toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FromFile fromFile = (FromFile) o;
        return Objects.equals(path, fromFile.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
