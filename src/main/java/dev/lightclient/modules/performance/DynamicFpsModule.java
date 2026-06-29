package dev.lightclient.modules.performance;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;
import org.lwjgl.glfw.GLFW;

/** Caps the framerate while the game window is unfocused to save resources. */
public final class DynamicFpsModule extends Module {
    private final NumberSetting unfocusedFps =
            register(new NumberSetting("Unfocused FPS", "FPS cap while window is inactive", 10, 1, 60, 1));

    private Integer originalMaxFps;
    private boolean reduced;

    public DynamicFpsModule() {
        super("Dynamic FPS", "Lowers FPS when the window is unfocused", Category.PERFORMANCE);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.options == null || mc.getWindow() == null) {
            return;
        }
        boolean focused = GLFW.glfwGetWindowAttrib(mc.getWindow().getHandle(), GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
        if (!focused && !reduced) {
            originalMaxFps = mc.options.getMaxFps().getValue();
            mc.options.getMaxFps().setValue(unfocusedFps.getInt());
            reduced = true;
        } else if (focused && reduced) {
            restore();
        }
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void restore() {
        if (originalMaxFps != null) {
            mc.options.getMaxFps().setValue(originalMaxFps);
            originalMaxFps = null;
        }
        reduced = false;
    }
}
