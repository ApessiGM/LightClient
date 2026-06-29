package dev.lightclient.modules.performance;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import net.minecraft.particle.ParticlesMode;

/** Forces minimal particle rendering while enabled to reduce overdraw. */
public final class ParticleOptimizationModule extends Module {
    private ParticlesMode original;

    public ParticleOptimizationModule() {
        super("Particle Optimization", "Minimises particle rendering", Category.PERFORMANCE);
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            original = mc.options.getParticles().getValue();
            mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null && original != null) {
            mc.options.getParticles().setValue(original);
            original = null;
        }
    }
}
