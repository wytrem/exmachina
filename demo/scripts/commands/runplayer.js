// A command that parses the given player name and run the given script for that player.
// It expects
// - "args" of type Java.type("String[]")
// - "sender" of type Java.type("org.bukkit.command.CommandSender")
// to be defined in the context.

// The require function is defined in the "globals/exmachina" script.
require("utils/bukkit");

if (args.length == 0) {
  sender.sendMessage(ChatColor.RED + "Please give a script name.");
} else if (args.length == 1) {
  sender.sendMessage(ChatColor.RED + "Please give a player name.");
} else {
  var playerName = args[1];
  var player = Bukkit.getPlayer(playerName);
  if (player == null) {
    sender.sendMessage(ChatColor.RED + "Player '" + playerName + "' not found.");
  }
  else {
    run(args[0], {
      "sender": sender,
      "args": java.util.Arrays.copyOfRange(args, 2, args.length),
      "player": player
    });
  }
}
