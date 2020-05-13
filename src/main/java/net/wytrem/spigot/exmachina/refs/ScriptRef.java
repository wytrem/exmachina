package net.wytrem.spigot.exmachina.refs;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.nio.file.Path;

/**
 * A script reference. Stores all the information needed to retrieve the source code.
 */
public abstract class ScriptRef implements ConfigurationSerializable {

    /**
     *
     * Creates a new {@link ScriptRef} from the given script source code.
     *
     * @param source The script source code
     * @return a new {@link ScriptRef} holding the given source code
     */
    public static ScriptRef inline(String source) {
        return new Inline(source);
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

        return new FromPath(path);
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
}
