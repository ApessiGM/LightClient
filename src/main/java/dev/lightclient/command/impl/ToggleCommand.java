package dev.lightclient.command.impl;

import dev.lightclient.command.Command;
import dev.lightclient.module.Module;

/** Toggles a module by name. */
public final class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "Toggles a module on or off", "toggle <module>", "t");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            error("Usage: " + getUsage());
            return;
        }
        String name = String.join(" ", args);
        Module module = client().getModuleManager().getModule(name);
        if (module == null) {
            error("Module '" + name + "' not found.");
            return;
        }
        module.toggle();
        send("§f" + module.getName() + " §7-> " + (module.isEnabled() ? "§aenabled" : "§cdisabled"));
    }
}
