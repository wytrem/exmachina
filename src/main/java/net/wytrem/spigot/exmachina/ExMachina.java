package net.wytrem.spigot.exmachina;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import net.wytrem.spigot.exmachina.refs.FromPath;
import net.wytrem.spigot.exmachina.refs.ScriptRef;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.script.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.IntStream;

public class ExMachina extends JavaPlugin {
    private static final String NASHORN = "nashorn";

    public static ExMachina instance;

    private ScriptEngine scriptEngine;
    private File scriptsFolder;

    // Config
    public String engineName;
    public String scriptFilesSuffix;
    public boolean useCache;

    // Scripts cache
    private LoadingCache<ScriptRef, Script> cache;
    private ScriptLoader loader;

    // Globals
    private Collection<Script> globals;

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();

        // Load config
        engineName = this.getConfig().getString("engine", NASHORN);
        scriptFilesSuffix = this.getConfig().getString("scriptFilesSuffix", ".js");
        useCache = this.getConfig().getBoolean("cache", true);


        getLogger().info("Setting up ScriptEngine");
        // Create the engine
        {
            // Work around the default nashorn ClassLoader
            // TODO: use a more general ClassLoader to allow accessing plugin classes without warning
            ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
            if (engineName.equals(NASHORN)) {
                Thread.currentThread().setContextClassLoader(this.getClassLoader());
            }

            this.scriptEngine = new ScriptEngineManager().getEngineByName(engineName);

            if (engineName.equals(NASHORN)) {
                Thread.currentThread().setContextClassLoader(previousClassLoader);
            }
        }

        if (this.scriptEngine == null) {
            getLogger().severe("Could not find ScriptEngine named '" + engineName + "'");
            this.getPluginLoader().disablePlugin(this);
        }
        else {
            getLogger().log(Level.FINE, "Successfully loaded " + this.scriptEngine.toString());
        }

        // Commands
        this.getCommand("script").setExecutor(this);

        // Creating script loader, and cache if needed
        this.scriptsFolder = new File(this.getDataFolder(), "scripts");
        this.loader = new ScriptLoader(this.scriptsFolder, this.scriptFilesSuffix);

        if (this.useCache) {
            getLogger().info("Setting up cache");
            this.cache = CacheBuilder.newBuilder().build(this.loader);
        }


        // Load globals
        {
            this.globals = new ArrayList<>();
            File globalsFolder = new File(this.scriptsFolder, "globals");

            if (globalsFolder.exists() && globalsFolder.isDirectory()) {
                for (File file : Files.fileTreeTraverser().breadthFirstTraversal(globalsFolder)) {
                    if (file.isFile()) {
                        ScriptRef ref = ScriptRef.fromPath(this.scriptsFolder.toPath().relativize(file.toPath()));
                        this.globals.add(this.load(ref));
                    }
                }
            }

            getLogger().info("Loaded " + this.globals.size() + " global(s)");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            String commandScriptName = args[0];
            String[] scriptArgs = Arrays.copyOfRange(args, 1, args.length);

            ScriptRef ref = ScriptRef.fromPath("commands/" + commandScriptName);

            try {
                Map<String, Object> moreBindings = new HashMap<>();
                moreBindings.put("sender", sender);
                moreBindings.put("args", scriptArgs);
                this.eval(ref, moreBindings);
            } catch (ScriptException e) {
                e.printStackTrace();
                sender.sendMessage("Error while running the command.");
            }

            return true;
        }
    }

    private ScriptContext createDefaultContext() throws ScriptException {
        // Build a new context based on the default one in the ScriptEngine
        ScriptContext context = new SimpleScriptContext();
        IntStream.of(ScriptContext.ENGINE_SCOPE, ScriptContext.GLOBAL_SCOPE).forEach(scope -> {
            Bindings bindings = this.scriptEngine.createBindings();
            this.scriptEngine.getBindings(scope).putAll(bindings);
            context.setBindings(bindings, scope);
        });

        // Populate the given context with globals
        for (Script global : this.globals) {
            // set applyGlobal to false to prevent infinite loop
            this.eval(global, context);
        }

        return context;
    }

    /**
     * Loads and runs the script pointed by the given {@link ScriptRef} in the default context.
     *
     * @return the result of the script, or null if there is none
     * @see ExMachina#load(ScriptRef)
     * @see ExMachina#eval(Script)
     */
    public Object eval(ScriptRef ref) throws ScriptException {
            return this.eval(this.load(ref));
    }

    /**
     * Loads and runs the script pointed by the given {@link ScriptRef} with the given presets.
     *
     * @return the result of the script, or null if there is none
     * @see ExMachina#load(ScriptRef)
     * @see ExMachina#eval(ScriptRef, Map)
     */
    public Object eval(ScriptRef ref, Map<String, Object> bindings) throws ScriptException  {
            return this.eval(this.load(ref), bindings);
    }

    /**
     * Loads and runs the script pointed by the given {@link ScriptRef} inside the given context.
     *
     * @return the result of the script, or null if there is none
     * @see ExMachina#load(ScriptRef)
     * @see ExMachina#eval(Script, ScriptContext)
     */
    public Object eval(ScriptRef ref, ScriptContext context) throws ScriptException {
            return this.eval(this.load(ref), context);
    }

    /**
     * Runs the given script in the default context.
     *
     * @return the result of the script, or null if there is none
     */
    public Object eval(Script script) throws ScriptException {
        return this.eval(script, Collections.emptyMap());
    }

    /**
     * Runs the given script with the given presets.
     *
     * @return the result of the script, or null if there is none
     */
    public Object eval(Script script, Map<String, Object> bindings) throws ScriptException {
        ScriptContext context = this.createDefaultContext();
        context.getBindings(ScriptContext.ENGINE_SCOPE).putAll(bindings);

        return this.eval(script, context);
    }

    /**
     * Runs the given script inside the given context.
     *
     * @return the result of the script, or null if there is none
     */
    private Object eval(Script script, ScriptContext context) throws ScriptException {
        if (script == null) {
            return null;
        }

        // Actually run the script on the ScriptEngine.
        return this.scriptEngine.eval(script.getSourceCode(), context);
    }

    /**
     * Reads and retrieves the script associated with the given key from the script folder (or the cache).
     */
    public Script load(ScriptRef ref) {
        if (this.useCache) {
            try {
                return this.cache.get(ref);
            } catch (ExecutionException e) {
                this.getLogger().log(Level.SEVERE, "Unable to load " + ref, e);
            }
        }
        else {
            try {
                return this.loader.load(ref);
            } catch (Exception e) {
                this.getLogger().log(Level.SEVERE, "Unable to load " + ref, e);
            }
        }

        return null;
    }

    /**
     * @return The script {@link File} for the current {@link ScriptLoader}
     */
    public File getScriptFile(ScriptRef ref) {
        if (ref instanceof FromPath) {
            return this.loader.getScriptFile(((FromPath) ref));
        }
        return null;
    }
}
