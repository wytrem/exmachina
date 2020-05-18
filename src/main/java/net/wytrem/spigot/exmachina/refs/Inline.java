package net.wytrem.spigot.exmachina.refs;

/**
 * References a "inlined" {@link net.wytrem.spigot.exmachina.Script}: stores its source code.
 */
public interface Inline extends ScriptRef {
    /**
     * @return the script source code
     */
    String getSource();
}
