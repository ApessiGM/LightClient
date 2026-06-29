package dev.lightclient.modules.performance;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/** Temporarily lowers the render distance while enabled to improve framerate. */
public final class ChunkOptimizationModule extends Module {
    private final NumberSetting renderDistance =
            register(new NumberSetting("Render Distance", "Chunks to render", 8, 2, 32, 1));

    private Integer original;

    public ChunkOptimizationModule() {
        super("Chunk Optimization", "Caps render distance for performance", Category.PERFORMANCE);
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            original = mc.options.getViewDistance().getValue();
            mc.options.getViewDistance().setValue(renderDistance.getInt());
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null && original != null) {
            mc.options.getViewDistance().setValue(original);
            original = null;
        }
    }

    @Override
    public String getDisplayInfo() {
        return renderDistance.getInt() + " chunks";
    }
}
