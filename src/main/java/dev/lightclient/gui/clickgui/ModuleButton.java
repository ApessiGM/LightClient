package dev.lightclient.gui.clickgui;

import dev.lightclient.LightClient;
import dev.lightclient.gui.clickgui.component.BooleanComponent;
import dev.lightclient.gui.clickgui.component.ColorComponent;
import dev.lightclient.gui.clickgui.component.Component;
import dev.lightclient.gui.clickgui.component.KeybindComponent;
import dev.lightclient.gui.clickgui.component.ModeComponent;
import dev.lightclient.gui.clickgui.component.NumberComponent;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;
import dev.lightclient.setting.ColorSetting;
import dev.lightclient.setting.ModeSetting;
import dev.lightclient.setting.NumberSetting;
import dev.lightclient.setting.Setting;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.animation.Animation;
import dev.lightclient.util.animation.Easing;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/** A single module row inside a {@link Panel}, with an expandable settings list. */
public final class ModuleButton {
    private static final double ROW_HEIGHT = 14;

    private final Module module;
    private final List<Component> components = new ArrayList<>();
    private final Animation toggleAnim = new Animation(160, Easing.EASE_OUT_EXPO);

    private double x;
    private double y;
    private double width;
    private boolean open;

    public ModuleButton(Module module) {
        this.module = module;
        for (Setting setting : module.getSettings()) {
            Component component = create(setting);
            if (component != null) {
                components.add(component);
            }
        }
        components.add(new KeybindComponent(module.getKeybind()));
        toggleAnim.animateTo(module.isEnabled() ? 1.0 : 0.0);
    }

    private Component create(Setting setting) {
        if (setting instanceof BooleanSetting b) {
            return new BooleanComponent(b);
        }
        if (setting instanceof NumberSetting n) {
            return new NumberComponent(n);
        }
        if (setting instanceof ModeSetting m) {
            return new ModeComponent(m);
        }
        if (setting instanceof ColorSetting c) {
            return new ColorComponent(c);
        }
        return null;
    }

    public void setBounds(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public double getTotalHeight() {
        double total = ROW_HEIGHT;
        if (open) {
            for (Component component : components) {
                if (component.isVisible()) {
                    total += component.getHeight();
                }
            }
            total += 2;
        }
        return total;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        toggleAnim.animateTo(module.isEnabled() ? 1.0 : 0.0);
        int accent = LightClient.getInstance().getThemeManager().accent();
        boolean hovered = hovered(mouseX, mouseY);

        int base = hovered ? 0xFF20202A : 0xFF18181F;
        RenderUtil.rect(context, x, y, width, ROW_HEIGHT, base);
        RenderUtil.rect(context, x, y, 2 + (width - 2) * toggleAnim.getValue(), ROW_HEIGHT,
                ColorUtil.withAlpha(accent, 45));

        int textColor = module.isEnabled() ? 0xFFFFFFFF : 0xFF9A9AA5;
        RenderUtil.text(context, module.getName(), x + 5, y + 3, textColor);
        String info = module.getDisplayInfo();
        if (!info.isEmpty()) {
            RenderUtil.text(context, info, x + width - RenderUtil.textWidth(info) - 10, y + 3, accent);
        }
        if (!components.isEmpty()) {
            RenderUtil.text(context, open ? "-" : "+", x + width - 7, y + 3, 0xFFB9B9C0);
        }

        if (open) {
            double cy = y + ROW_HEIGHT + 1;
            for (Component component : components) {
                if (!component.isVisible()) {
                    continue;
                }
                component.setBounds(x + 2, cy, width - 4);
                component.render(context, mouseX, mouseY);
                cy += component.getHeight();
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + ROW_HEIGHT) {
            if (button == 0) {
                module.toggle();
            } else if (button == 1) {
                open = !open;
            }
            return true;
        }
        if (open) {
            for (Component component : components) {
                if (component.isVisible() && component.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean keyPressed(int keyCode) {
        if (!open) {
            return false;
        }
        for (Component component : components) {
            if (component.keyPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }

    private boolean hovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + ROW_HEIGHT;
    }

    public Module getModule() {
        return module;
    }
}
