package dev.lightclient.gui.clickgui.component;

import dev.lightclient.LightClient;
import dev.lightclient.setting.KeybindSetting;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

/** Lets the user rebind a {@link KeybindSetting} by clicking and pressing a key. */
public final class KeybindComponent extends Component {
    private final KeybindSetting keybind;
    private boolean listening;

    public KeybindComponent(KeybindSetting setting) {
        super(setting);
        this.keybind = setting;
        this.height = 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        int accent = LightClient.getInstance().getThemeManager().accent();
        RenderUtil.text(context, keybind.getName(), x + 4, y + 3, 0xFFD8D8DF);
        String label = listening ? "..." : keyName();
        RenderUtil.text(context, "[" + label + "]", x + width - RenderUtil.textWidth("[" + label + "]") - 5,
                y + 3, accent);
    }

    private String keyName() {
        if (!keybind.isBound()) {
            return "None";
        }
        return InputUtil.Type.KEYSYM.createFromCode(keybind.get()).getLocalizedText().getString().toUpperCase();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered(mouseX, mouseY)) {
            listening = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode) {
        if (!listening) {
            return false;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_DELETE) {
            keybind.set(GLFW.GLFW_KEY_UNKNOWN);
        } else {
            keybind.set(keyCode);
        }
        listening = false;
        return true;
    }
}
