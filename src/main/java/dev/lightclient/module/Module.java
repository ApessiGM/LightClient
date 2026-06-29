package dev.lightclient.module;

import dev.lightclient.LightClient;
import dev.lightclient.setting.KeybindSetting;
import dev.lightclient.setting.Setting;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for every feature in the client. A module owns a list of
 * {@link Setting}s, an enabled flag and a keybind. Subclasses override the
 * lifecycle hooks to implement behaviour.
 */
public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting> settings = new ArrayList<>();
    private final KeybindSetting keybind;

    private boolean enabled;

    protected Module(String name, String description, Category category) {
        this(name, description, category, GLFW.GLFW_KEY_UNKNOWN);
    }

    protected Module(String name, String description, Category category, int defaultKey) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keybind = new KeybindSetting("Keybind", "Key that toggles this module", defaultKey);
    }

    protected <T extends Setting> T register(T setting) {
        settings.add(setting);
        return setting;
    }

    public final void setEnabled(boolean state) {
        if (this.enabled == state) {
            return;
        }
        this.enabled = state;
        if (state) {
            try {
                onEnable();
            } catch (Exception ignored) {
            }
            LightClient.getInstance().getEventBus().subscribe(this);
        } else {
            LightClient.getInstance().getEventBus().unsubscribe(this);
            try {
                onDisable();
            } catch (Exception ignored) {
            }
        }
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public KeybindSetting getKeybind() {
        return keybind;
    }

    public int getKey() {
        return keybind.get();
    }

    public void setKey(int key) {
        keybind.set(key);
    }

    /** Text shown next to the module name in the ClickGUI (e.g. mode). */
    public String getDisplayInfo() {
        return "";
    }

    // --- Lifecycle hooks (override as needed) ---

    public void onEnable() {
    }

    public void onDisable() {
    }
}
