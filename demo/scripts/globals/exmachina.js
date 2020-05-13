var ExMachina = Java.type("net.wytrem.spigot.exmachina.ExMachina");
var ScriptRef = Java.type("net.wytrem.spigot.exmachina.refs.ScriptRef");

function run(scriptPath) {
    return ExMachina.instance.eval(ScriptRef.fromPath(scriptPath));
}

function run(scriptPath, bindings) {
    var bindingsMap = new java.util.HashMap();
    for (var key in bindings) {
        bindingsMap.put(key, bindings[key]);
    }
    return ExMachina.instance.eval(ScriptRef.fromPath(scriptPath), bindingsMap);
}

function require(scriptPath) {
    load(ExMachina.instance.getScriptFile(ScriptRef.fromPath(scriptPath)));
}

function removeFirstElements(array, count) {
    if (args.length < count) {
        return java.util.Arrays.copyOf(array);
    }
    else {
        return java.util.Arrays.copyOfRange(array, count, array.length);
    }
}

function removeFirstArg(args) {
    return removeFirstelements(args, 1);
}
