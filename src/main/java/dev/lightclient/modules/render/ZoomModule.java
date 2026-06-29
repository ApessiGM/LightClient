package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/**
 * Reduces the field of view while enabled to zoom in. The original FOV is
 * restored when the module is turned off.
 */
public final class ZoomModule extends Module {
    private final NumberSetting amount = register(new NumberSetting("Amount", "Zoom factor", 4.0, 2.0, 10.0, 0.5));

    private Integer originalFov;

    public ZoomModule() {
        super("Zoom", "Zoom in to see distant targets", Category.RENDER, org.lwjgl.glfw.GLFW.GLFW_KEY_C);
    }

    @Override
    public void onEnable() {
        if (mc.options == null) {
            return;
        }
        originalFov = mc.options.getFov().getValue();
        int zoomed = Math.max(1, (int) Math.round(originalFov / amount.get()));
        mc.options.getFov().setValue(zoomed);
    }

    @Override
    public void onDisable() {
        if (mc.options != null && originalFov != null) {
            mc.options.getFov().setValue(originalFov);
            originalFov = null;
        }
    }

    @Override
    public String getDisplayInfo() {
        return amount.getInt() + "x";
    }
}
