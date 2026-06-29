package dev.lightclient.command.impl;

import dev.lightclient.command.Command;

/** Lists all available commands. */
public final class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Lists all commands", "help", "?", "commands");
    }

    @Override
    public void execute(String[] args) {
        send("§dAvailable commands:");
        for (Command command : client().getCommandManager().getCommands()) {
            send("§5" + command.getUsage() + " §7- §f" + command.getDescription());
        }
    }
}
