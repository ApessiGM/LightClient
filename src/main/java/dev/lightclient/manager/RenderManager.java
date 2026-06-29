package dev.lightclient.manager;

import dev.lightclient.LightClient;
import dev.lightclient.event.events.Render2DEvent;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

/**
 * Bridges Fabric's HUD render callback into the client: posts a 2D render
 * event for modules, then draws HUD elements and notifications on top.
 */
public final class RenderManager {

    public void register() {
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.options.hudHidden || mc.player == null) {
                return;
            }
            float delta = tickCounter.getDynamicDeltaTicks();
            LightClient client = LightClient.getInstance();
            client.getClickTracker().update();
            client.getEventBus().post(new Render2DEvent(context, delta));
            client.getHudManager().render(context);
            client.getNotificationManager().render(context);
        });
    }
}
