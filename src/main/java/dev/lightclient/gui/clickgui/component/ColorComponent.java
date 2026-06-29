package dev.lightclient.gui.clickgui.component;

import dev.lightclient.setting.ColorSetting;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * Displays a colour swatch and exposes hue/brightness adjustment by dragging
 * a compact picker that expands on click.
 */
public final class ColorComponent extends Component {
    private final ColorSetting color;
    private boolean expanded;
    private float hue;
    private float saturation = 1f;
    private float brightness = 1f;
    private boolean draggingField;

    public ColorComponent(ColorSetting setting) {
        super(setting);
        this.color = setting;
        this.height = 14;
        float[] hsb = java.awt.Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    public double getHeight() {
        return expanded ? 14 + 54 : 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        RenderUtil.text(context, color.getName(), x + 4, y + 3, 0xFFD8D8DF);
        double swatch = 9;
        double swatchX = x + width - swatch - 5;
        RenderUtil.roundedRect(context, swatchX, y + 2, swatch, swatch, 2, color.get());

        if (expanded) {
            renderPicker(context, mouseX, mouseY);
        }
    }

    private void renderPicker(DrawContext context, int mouseX, int mouseY) {
        double fieldX = x + 4;
        double fieldY = y + 16;
        double fieldW = width - 28;
        double fieldH = 40;
        // Saturation/brightness field.
        for (int i = 0; i < fieldW; i++) {
            float s = (float) (i / fieldW);
            int top = ColorUtil.hsb(hue, s, 1f, 255);
            int bottom = ColorUtil.hsb(hue, s, 0f, 255);
            RenderUtil.gradientV(context, fieldX + i, fieldY, 1, fieldH, top, bottom);
        }
        // Hue strip.
        double hueX = fieldX + fieldW + 4;
        for (int i = 0; i < fieldH; i++) {
            float h = (float) (i / fieldH);
            RenderUtil.rect(context, hueX, fieldY + i, 12, 1, ColorUtil.hsb(h, 1f, 1f, 255));
        }
        if (draggingField) {
            updateField(mouseX, mouseY, fieldX, fieldY, fieldW, fieldH);
        }
    }

    private void updateField(double mouseX, double mouseY, double fieldX, double fieldY, double fieldW, double fieldH) {
        saturation = (float) Math.max(0, Math.min(1, (mouseX - fieldX) / fieldW));
        brightness = (float) Math.max(0, Math.min(1, 1 - (mouseY - fieldY) / fieldH));
        color.set(ColorUtil.hsb(hue, saturation, brightness, color.getAlpha()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseY >= y && mouseY <= y + 14 && mouseX >= x && mouseX <= x + width) {
            if (button == 1) {
                expanded = !expanded;
                return true;
            }
        }
        if (expanded) {
            double fieldX = x + 4;
            double fieldY = y + 16;
            double fieldW = width - 28;
            double fieldH = 40;
            if (mouseX >= fieldX && mouseX <= fieldX + fieldW && mouseY >= fieldY && mouseY <= fieldY + fieldH) {
                draggingField = true;
                updateField(mouseX, mouseY, fieldX, fieldY, fieldW, fieldH);
                return true;
            }
            double hueX = fieldX + fieldW + 4;
            if (mouseX >= hueX && mouseX <= hueX + 12 && mouseY >= fieldY && mouseY <= fieldY + fieldH) {
                hue = (float) Math.max(0, Math.min(1, (mouseY - fieldY) / fieldH));
                color.set(ColorUtil.hsb(hue, saturation, brightness, color.getAlpha()));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingField = false;
        return false;
    }
}
