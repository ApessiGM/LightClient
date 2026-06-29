package dev.lightclient.modules.combat;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;

/** Spawns particles on entities that you successfully hit. */
public final class HitParticlesModule extends Module {
    private final ModeSetting particle =
            register(new ModeSetting("Particle", "Particle type", "Crit", "Crit", "Magic", "Flame", "Heart"));

    private final java.util.Map<Integer, Float> lastHealth = new java.util.HashMap<>();

    public HitParticlesModule() {
        super("Hit Particles", "Particles when you damage an entity", Category.COMBAT);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.world == null || mc.player == null) {
            return;
        }
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity living) || entity == mc.player) {
                continue;
            }
            float health = living.getHealth();
            Float previous = lastHealth.put(entity.getId(), health);
            if (previous != null && health < previous && mc.player.distanceTo(entity) < 6.0) {
                spawn(entity);
            }
        }
    }

    private void spawn(Entity entity) {
        if (mc.world == null) {
            return;
        }
        var type = switch (particle.get()) {
            case "Magic" -> ParticleTypes.WITCH;
            case "Flame" -> ParticleTypes.FLAME;
            case "Heart" -> ParticleTypes.HEART;
            default -> ParticleTypes.CRIT;
        };
        for (int i = 0; i < 6; i++) {
            mc.world.addParticleClient(type,
                    entity.getX() + (mc.world.random.nextDouble() - 0.5),
                    entity.getY() + entity.getHeight() * 0.6,
                    entity.getZ() + (mc.world.random.nextDouble() - 0.5),
                    0, 0.1, 0);
        }
    }

    @Override
    public void onDisable() {
        lastHealth.clear();
    }
}
