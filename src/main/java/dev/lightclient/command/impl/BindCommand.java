package dev.lightclient.command.impl;

import dev.lightclient.command.Command;
import dev.lightclient.module.Module;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/** Binds or clears a key for a module. */
public final class BindCommand extends Command {
    public BindCommand() {
        super("bind", "Binds a key to a module", "bind <module> <key|none>", "b");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            error("Usage: " + getUsage());
            return;
        }
        String keyToken = args[args.length - 1];
        String name = String.join(" ", java.util.Arrays.copyOf(args, args.length - 1));
        Module module = client().getModuleManager().getModule(name);
        if (module == null) {
            error("Module '" + name + "' not found.");
            return;
        }
        if (keyToken.equalsIgnoreCase("none")) {
            module.setKey(GLFW.GLFW_KEY_UNKNOWN);
            send("Cleared keybind for §f" + module.getName());
            return;
        }
        int key = resolveKey(keyToken);
        if (key == GLFW.GLFW_KEY_UNKNOWN) {
            error("Unknown key: " + keyToken);
            return;
        }
        module.setKey(key);
        send("Bound §f" + module.getName() + " §7to §d" + keyToken.toUpperCase());
    }

    private int resolveKey(String token) {
        try {
            return InputUtil.fromTranslationKey("key.keyboard." + token.toLowerCase()).getCode();
        } catch (Exception exception) {
            return GLFW.GLFW_KEY_UNKNOWN;
        }
    }
}
