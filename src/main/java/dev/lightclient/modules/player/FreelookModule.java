package dev.lightclient.modules.player;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;
import net.minecraft.client.option.Perspective;
import org.lwjgl.glfw.GLFW;

/**
 * Lets you look around freely without changing your movement direction by
 * temporarily switching to a third-person perspective while held.
 */
public final class FreelookModule extends Module {
    private final NumberSetting sensitivity =
            register(new NumberSetting("Sensitivity", "Look sensitivity multiplier", 1.0, 0.1, 2.0, 0.1));

    private Perspective previous;

    public FreelookModule() {
        super("Freelook", "Look around without turning", Category.PLAYER, GLFW.GLFW_KEY_V);
    }

    @Override
    public void onEnable() {
        if (mc.options == null) {
            return;
        }
        previous = mc.options.getPerspective();
        mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onDisable() {
        if (mc.options != null && previous != null) {
            mc.options.setPerspective(previous);
            previous = null;
        }
    }

    public float getSensitivity() {
        return sensitivity.getFloat();
    }
}
