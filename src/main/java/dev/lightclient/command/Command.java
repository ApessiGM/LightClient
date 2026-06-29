package dev.lightclient.command;

import dev.lightclient.LightClient;
import dev.lightclient.Reference;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

/** Base class for an in-game chat command (prefixed by {@code .}). */
public abstract class Command {
    protected final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final String usage;
    private final List<String> aliases;

    protected Command(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void execute(String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return Reference.COMMAND_PREFIX + usage;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean matches(String label) {
        if (label.equalsIgnoreCase(name)) {
            return true;
        }
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(label)) {
                return true;
            }
        }
        return false;
    }

    protected void send(String message) {
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal("§8[§5Light§dClient§8] §r" + message), false);
        }
    }

    protected void error(String message) {
        send("§c" + message);
    }

    protected LightClient client() {
        return LightClient.getInstance();
    }
}
