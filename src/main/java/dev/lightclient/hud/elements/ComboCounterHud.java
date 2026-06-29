package dev.lightclient.hud.elements;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;

import java.util.List;

/** Tracks consecutive hits on the entity under the crosshair. */
public final class ComboCounterHud extends HudModule {
    private int combo;
    private int targetId = -1;
    private float lastHealth;
    private long lastHit;

    public ComboCounterHud() {
        super("Combo Counter", "Counts consecutive hits", 150, 142);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        if (System.currentTimeMillis() - lastHit > 3000) {
            combo = 0;
        }
        if (mc.crosshairTarget instanceof EntityHitResult hit) {
            Entity entity = hit.getEntity();
            if (entity instanceof LivingEntity living) {
                if (living.getId() != targetId) {
                    targetId = living.getId();
                    lastHealth = living.getHealth();
                } else if (living.getHealth() < lastHealth) {
                    combo++;
                    lastHit = System.currentTimeMillis();
                    lastHealth = living.getHealth();
                }
            }
        }
    }

    @Override
    public void render(DrawContext context) {
        drawPanel(context, List.of("Combo: " + combo));
    }
}
