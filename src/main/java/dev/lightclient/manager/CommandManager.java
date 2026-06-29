package dev.lightclient.manager;

import dev.lightclient.Reference;
import dev.lightclient.command.Command;
import dev.lightclient.command.impl.BindCommand;
import dev.lightclient.command.impl.ConfigCommand;
import dev.lightclient.command.impl.FriendCommand;
import dev.lightclient.command.impl.HelpCommand;
import dev.lightclient.command.impl.ThemeCommand;
import dev.lightclient.command.impl.ToggleCommand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/** Registers and dispatches chat commands prefixed with {@link Reference#COMMAND_PREFIX}. */
public final class CommandManager {
    private final List<Command> commands = new ArrayList<>();

    public void init() {
        register(new HelpCommand());
        register(new BindCommand());
        register(new ToggleCommand());
        register(new FriendCommand());
        register(new ConfigCommand());
        register(new ThemeCommand());
    }

    public void register(Command command) {
        commands.add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public boolean isCommand(String message) {
        return message.startsWith(Reference.COMMAND_PREFIX);
    }

    /**
     * Handles a chat message. Returns {@code true} if it was a command and the
     * outgoing chat message should be cancelled.
     */
    public boolean handle(String message) {
        if (!isCommand(message)) {
            return false;
        }
        String body = message.substring(Reference.COMMAND_PREFIX.length()).trim();
        if (body.isEmpty()) {
            return true;
        }
        String[] parts = body.split("\\s+");
        String label = parts[0];
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        for (Command command : commands) {
            if (command.matches(label)) {
                try {
                    command.execute(args);
                } catch (Exception exception) {
                    feedback("§cError running command: " + exception.getMessage());
                }
                return true;
            }
        }
        feedback("§cUnknown command. Type §d" + Reference.COMMAND_PREFIX + "help§c for a list.");
        return true;
    }

    private void feedback(String message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal("§8[§5Light§dClient§8] §r" + message), false);
        }
    }
}
