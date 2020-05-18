package net.wytrem.spigot.exmachina;

import com.google.common.cache.CacheLoader;
import com.google.common.io.Files;
import net.wytrem.spigot.exmachina.refs.FromFile;
import net.wytrem.spigot.exmachina.refs.Inline;
import net.wytrem.spigot.exmachina.refs.ScriptRef;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ScriptLoader extends CacheLoader<ScriptRef, Script> {

    private final File scriptsFolder;
    public final String scriptFilesSuffix;

    public ScriptLoader(File scriptsFolder, String scriptFilesSuffix) {
        this.scriptsFolder = scriptsFolder;
        this.scriptFilesSuffix = scriptFilesSuffix;
    }


    /**
     * Computes or retrieves the value corresponding to {@code key}.
     *
     * @param key the non-null key whose value should be loaded
     * @return the value associated with {@code key}; <b>must not be null</b>
     * @throws Exception            if unable to load the result
     * @throws InterruptedException if this method is interrupted. {@code InterruptedException} is
     *                              treated like any other {@code Exception} in all respects except that, when it is caught,
     *                              the thread's interrupt status is set
     */
    @Override
    public Script load(ScriptRef key) throws Exception {
        if (key instanceof FromFile) {
            FromFile fromPath = (FromFile) key;

            return new Script(Files.toString(getScriptFile(fromPath), StandardCharsets.UTF_8));
        }
        else if (key instanceof Inline) {
            return new Script(((Inline) key).getSource());
        }
        else {
            throw new UnsupportedOperationException("Unknown ScriptRef type: " + key.getClass());
        }
    }

    public File getScriptFile(FromFile ref) {
        return this.scriptsFolder.toPath().resolve(ref.getPath() + this.scriptFilesSuffix).toFile();
    }
}
