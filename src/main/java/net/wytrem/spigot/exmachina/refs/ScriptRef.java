package net.wytrem.spigot.exmachina;

import com.google.common.base.MoreObjects;

import java.nio.file.Path;
import java.util.Objects;

/**
 * A script reference. Stores all the information needed to retrieve the source code.
 */
public class ScriptRef {

    /**
     * The script's path, relative to the scripts/ folder and without extension.
     */
    private final String path;

    private ScriptRef(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /**
     * Creates a new {@link ScriptRef} from the script path.
     *
     * @param path The script's path, relative to the scripts/ folder and without extension
     * @return a new {@link ScriptRef} pointing to the given path
     */
    public static ScriptRef fromPath(String path) {
        // TODO: fix this better
        // Remove extension if any.
        path = path.replaceFirst("[.][^.]+$", "");

        return new ScriptRef(path);
    }

    /**
     * Creates a new {@link ScriptRef} from the script path.
     *
     * @param path The script's path, relative to the scripts/ folder and without extension
     * @return a new {@link ScriptRef} pointing to the given path
     */
    public static ScriptRef fromPath(Path path) {
        return fromPath(path.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptRef scriptRef = (ScriptRef) o;
        return Objects.equals(path, scriptRef.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", path)
                .toString();
    }
}
