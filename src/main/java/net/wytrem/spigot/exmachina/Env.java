package net.wytrem.spigot.exmachina;

public class Env {
    public static final String BASE = "var ExMachina = Java.type(\"net.wytrem.spigot.exmachina.ExMachina\");" +
            "var ScriptRef = Java.type(\"net.wytrem.spigot.exmachina.ScriptRef\");" +
            "function run(scriptPath) {" +
            "  return ExMachina.instance.eval(ScriptRef.fromPath(scriptPath));" +
            "}" +
            "" +
            "function run(scriptPath, bindings) {" +
            "  var bindingsMap = new java.util.HashMap();" +
            "  for (var key in bindings) {" +
            "    bindingsMap.put(key, bindings[key]);" +
            "  }" +
            "  return ExMachina.instance.eval(ScriptRef.fromPath(scriptPath), bindingsMap);" +
            "}" +
            "" +
            "function require(scriptPath) {" +
            "  load(ExMachina.instance.getScriptFile(ScriptRef.fromPath(scriptPath)));" +
            "}" +
            "" +
            "function removeFirstArg(args) {" +
            "  if (args.length == 0) {" +
            "    return java.util.Arrays.copyOf(args);" +
            "  }" +
            "  else {" +
            "    return java.util.Arrays.copyOfRange(args, 1, args.length);" +
            "  }" +
            "}";
}
