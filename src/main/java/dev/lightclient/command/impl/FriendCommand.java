package dev.lightclient.command.impl;

import dev.lightclient.command.Command;

/** Manages the friend list. */
public final class FriendCommand extends Command {
    public FriendCommand() {
        super("friend", "Add, remove or list friends", "friend <add|remove|list> [name]", "f");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            error("Usage: " + getUsage());
            return;
        }
        String action = args[0].toLowerCase();
        switch (action) {
            case "add" -> {
                if (args.length < 2) {
                    error("Specify a name.");
                    return;
                }
                if (client().getFriendManager().add(args[1])) {
                    send("Added §d" + args[1] + " §7as a friend.");
                } else {
                    error(args[1] + " is already a friend.");
                }
            }
            case "remove", "del", "delete" -> {
                if (args.length < 2) {
                    error("Specify a name.");
                    return;
                }
                if (client().getFriendManager().remove(args[1])) {
                    send("Removed §d" + args[1] + " §7from friends.");
                } else {
                    error(args[1] + " is not a friend.");
                }
            }
            case "list" -> {
                if (client().getFriendManager().getFriends().isEmpty()) {
                    send("You have no friends added.");
                } else {
                    send("Friends: §d" + String.join("§7, §d", client().getFriendManager().getFriends()));
                }
            }
            default -> error("Usage: " + getUsage());
        }
    }
}
