package dev.lightclient.gui.clickgui.component;

import dev.lightclient.setting.Setting;
import net.minecraft.client.gui.DrawContext;

/** Base class for a single setting widget rendered inside a module's dropdown. */
public abstract class Component {
    protected final Setting setting;
    protected double x;
    protected double y;
    protected double width;
    protected double height = 12;

    protected Component(Setting setting) {
        this.setting = setting;
    }

    public void setBounds(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isVisible() {
        return setting.isVisible();
    }

    public abstract void render(DrawContext context, int mouseX, int mouseY);

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public void mouseDragged(double mouseX, double mouseY) {
    }

    public boolean keyPressed(int keyCode) {
        return false;
    }

    protected boolean hovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
