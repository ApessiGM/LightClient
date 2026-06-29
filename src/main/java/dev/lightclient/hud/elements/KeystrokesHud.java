package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

/** Renders WASD, mouse button and jump key indicators. */
public final class KeystrokesHud extends HudModule {
    private static final int KEY = 16;
    private static final int GAP = 2;

    public KeystrokesHud() {
        super("Keystrokes", "Shows pressed movement keys", 4, 110);
        setSize(KEY * 3 + GAP * 2, KEY * 3 + GAP * 2 + 8);
    }

    @Override
    public void render(DrawContext context) {
        double x = getX();
        double y = getY();
        int row = KEY + GAP;

        drawKey(context, x + row, y, "W", mc.options.forwardKey);
        drawKey(context, x, y + row, "A", mc.options.leftKey);
        drawKey(context, x + row, y + row, "S", mc.options.backKey);
        drawKey(context, x + row * 2, y + row, "D", mc.options.rightKey);

        double mouseY = y + row * 2;
        drawWide(context, x, mouseY, KEY, "L", mc.options.attackKey.isPressed());
        drawWide(context, x + row, mouseY, KEY * 2 + GAP - row, "R", mc.options.useKey.isPressed());

        double spaceY = mouseY + row;
        drawWide(context, x, spaceY, KEY * 3 + GAP * 2, "_", mc.options.jumpKey.isPressed());
        setSize(KEY * 3 + GAP * 2, (spaceY + 8) - y);
    }

    private void drawKey(DrawContext context, double x, double y, String label, KeyBinding binding) {
        drawWide(context, x, y, KEY, label, binding.isPressed());
    }

    private void drawWide(DrawContext context, double x, double y, double width, String label, boolean pressed) {
        int bg = pressed ? accent() : getBackgroundColor(0x1A1A1A);
        int fg = pressed ? 0xFF121212 : 0xFFFFFFFF;
        RenderUtil.roundedRect(context, x, y, width, label.equals("_") ? 6 : KEY, 2, bg);
        if (!label.equals("_")) {
            RenderUtil.textCentered(context, label, x + width / 2.0, y + (KEY - RenderUtil.fontHeight()) / 2.0, fg);
        }
    }
}
