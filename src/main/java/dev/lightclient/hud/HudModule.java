package dev.lightclient.hud;

import dev.lightclient.LightClient;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/**
 * Base class for a draggable, scalable HUD element. Position is stored in
 * GUI-scaled coordinates so elements stay put across resolution changes.
 */
public abstract class HudModule extends Module {
    private final NumberSetting scale = register(new NumberSetting("Scale", "Element scale", 1.0, 0.5, 3.0, 0.1));
    private final NumberSetting bgOpacity =
            register(new NumberSetting("Background", "Background opacity", 120, 0, 255, 5));

    private double x;
    private double y;
    private double width = 60;
    private double height = 12;

    protected HudModule(String name, String description, double defaultX, double defaultY) {
        super(name, description, Category.HUD);
        this.x = defaultX;
        this.y = defaultY;
    }

    @Override
    public void onEnable() {
        LightClient.getInstance().getHudManager().add(this);
    }

    @Override
    public void onDisable() {
        // Element stays registered; rendering simply skips disabled modules.
    }

    /** Renders this element at its current position. */
    public abstract void render(DrawContext context);

    public double getScale() {
        return scale.get();
    }

    public int getBackgroundColor(int rgb) {
        return ((int) bgOpacity.get() << 24) | (rgb & 0xFFFFFF);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getWidth() {
        return width * getScale();
    }

    public double getHeight() {
        return height * getScale();
    }

    protected void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Draws a themed panel containing the given text lines and updates this
     * element's size to match the content.
     */
    protected void drawPanel(DrawContext context, List<String> lines) {
        int pad = 3;
        int lineHeight = RenderUtil.fontHeight() + 1;
        int contentWidth = 0;
        for (String line : lines) {
            contentWidth = Math.max(contentWidth, RenderUtil.textWidth(line));
        }
        double panelWidth = contentWidth + pad * 2 + 2;
        double panelHeight = lines.size() * lineHeight + pad * 2 - 1;
        setSize(panelWidth, panelHeight);

        int accent = LightClient.getInstance().getThemeManager().accent();
        RenderUtil.roundedRect(context, getX(), getY(), panelWidth, panelHeight, 3, getBackgroundColor(0x121212));
        RenderUtil.rect(context, getX(), getY(), 2, panelHeight, accent);

        double textY = getY() + pad;
        for (String line : lines) {
            RenderUtil.textShadow(context, line, getX() + pad + 2, textY, 0xFFFFFFFF);
            textY += lineHeight;
        }
    }

    protected int accent() {
        return LightClient.getInstance().getThemeManager().accent();
    }
}
