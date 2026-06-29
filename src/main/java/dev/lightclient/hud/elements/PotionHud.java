package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.List;

/** Lists the player's active status effects with remaining time. */
public final class PotionHud extends HudModule {
    public PotionHud() {
        super("Potion HUD", "Shows active potion effects", 220, 26);
    }

    @Override
    public void render(DrawContext context) {
        List<String> lines = new ArrayList<>();
        if (mc.player != null) {
            for (StatusEffectInstance instance : mc.player.getStatusEffects()) {
                String name = instance.getEffectType().value().getName().getString();
                int amplifier = instance.getAmplifier() + 1;
                lines.add(name + " " + amplifier + " - " + formatDuration(instance.getDuration()));
            }
        }
        if (lines.isEmpty()) {
            lines.add("No active effects");
        }
        drawPanel(context, lines);
    }

    private String formatDuration(int ticks) {
        int seconds = ticks / 20;
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }
}
