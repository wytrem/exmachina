// A command that runs another script.
//
// It expects
// - "args" of type Java.type("String[]")
// - "sender" of type Java.type("org.bukkit.command.CommandSender")
// to be defined in the context.

if (args.length == 0) {
  sender.sendMessage("Please give a script path inside the 'scripts/' folder.");
} else {
  // Run the specified script and pass on next arguments.
  run(args[0], {
    "args": removeFirstArg(args)
  });
}
