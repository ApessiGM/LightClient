package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ModeSetting;
import org.lwjgl.glfw.GLFW;

/** Plays a selectable emote animation on a keybind. */
public final class EmotesModule extends Module {
    private final ModeSetting emote = register(new ModeSetting("Emote", "Emote to play", "Wave", "Wave", "Dab", "Sit", "Point"));

    public EmotesModule() {
        super("Emotes", "Player emote animations", Category.COSMETICS, GLFW.GLFW_KEY_B);
    }

    public String getEmote() {
        return emote.get();
    }
}
