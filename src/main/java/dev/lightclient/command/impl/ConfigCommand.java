package dev.lightclient.command.impl;

import dev.lightclient.command.Command;
import dev.lightclient.manager.ConfigManager;

import java.nio.file.Path;

/** Saves, loads, imports and exports configuration profiles. */
public final class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config", "Manage configuration profiles", "config <save|load|list|export|import> [name]", "cfg");
    }

    @Override
    public void execute(String[] args) {
        ConfigManager config = client().getConfigManager();
        if (args.length == 0) {
            error("Usage: " + getUsage());
            return;
        }
        String action = args[0].toLowerCase();
        switch (action) {
            case "save" -> {
                if (args.length >= 2) {
                    config.saveProfile(args[1]);
                    send("Saved profile §d" + args[1]);
                } else {
                    config.save();
                    send("Saved current configuration.");
                }
            }
            case "load" -> {
                if (args.length >= 2) {
                    if (config.loadProfile(args[1])) {
                        send("Loaded profile §d" + args[1]);
                    } else {
                        error("Profile '" + args[1] + "' not found.");
                    }
                } else {
                    config.load();
                    send("Loaded current configuration.");
                }
            }
            case "list" -> {
                if (config.listProfiles().isEmpty()) {
                    send("No saved profiles.");
                } else {
                    send("Profiles: §d" + String.join("§7, §d", config.listProfiles()));
                }
            }
            case "export" -> {
                if (args.length < 2) {
                    error("Specify a file path.");
                    return;
                }
                if (config.export(Path.of(args[1]))) {
                    send("Exported configuration to §d" + args[1]);
                } else {
                    error("Failed to export.");
                }
            }
            case "import" -> {
                if (args.length < 2) {
                    error("Specify a file path.");
                    return;
                }
                if (config.importFrom(Path.of(args[1]))) {
                    send("Imported configuration from §d" + args[1]);
                } else {
                    error("Failed to import (file not found?).");
                }
            }
            default -> error("Usage: " + getUsage());
        }
    }
}
