package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Displays the current framerate. */
public final class FpsHud extends HudModule {
    public FpsHud() {
        super("FPS HUD", "Shows current FPS", 4, 4);
    }

    @Override
    public void render(DrawContext context) {
        drawPanel(context, List.of("FPS: " + mc.getCurrentFps()));
    }
}
