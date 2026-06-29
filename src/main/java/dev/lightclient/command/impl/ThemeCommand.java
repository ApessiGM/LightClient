package dev.lightclient.command.impl;

import dev.lightclient.command.Command;

/** Switches or lists UI themes. */
public final class ThemeCommand extends Command {
    public ThemeCommand() {
        super("theme", "Change the UI theme", "theme <name|list>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            error("Usage: " + getUsage());
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            send("Themes: §d" + String.join("§7, §d", client().getThemeManager().getThemes().keySet()));
            return;
        }
        String name = String.join(" ", args);
        if (client().getThemeManager().setActive(name)) {
            send("Theme set to §d" + client().getThemeManager().getActive().name);
        } else {
            error("Theme '" + name + "' not found. Use §dtheme list§c.");
        }
    }
}
