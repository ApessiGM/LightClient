package dev.lightclient.modules.render;

import dev.lightclient.Reference;
import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.Render2DEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;
import dev.lightclient.setting.ColorSetting;
import dev.lightclient.setting.NumberSetting;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.Perspective;

/** A fully customisable crosshair drawn over the vanilla one. */
public final class CrosshairModule extends Module {
    private final NumberSetting length = register(new NumberSetting("Length", "Line length", 5, 1, 15, 1));
    private final NumberSetting gap = register(new NumberSetting("Gap", "Centre gap", 2, 0, 10, 1));
    private final NumberSetting thickness = register(new NumberSetting("Thickness", "Line thickness", 1, 1, 4, 1));
    private final BooleanSetting dot = register(new BooleanSetting("Dot", "Draw a centre dot", false));
    private final ColorSetting color =
            register(new ColorSetting("Color", "Crosshair colour", ColorUtil.opaque(Reference.COLOR_PURPLE_BRIGHT)));

    public CrosshairModule() {
        super("Crosshair Editor", "Customisable crosshair", Category.RENDER);
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        if (mc.player == null || mc.options.getPerspective() != Perspective.FIRST_PERSON || mc.currentScreen != null) {
            return;
        }
        DrawContext ctx = event.getContext();
        int cx = ctx.getScaledWindowWidth() / 2;
        int cy = ctx.getScaledWindowHeight() / 2;
        int g = gap.getInt();
        int len = length.getInt();
        int t = thickness.getInt();
        int c = color.get();

        RenderUtil.rect(ctx, cx - g - len, cy - t / 2.0, len, t, c);
        RenderUtil.rect(ctx, cx + g, cy - t / 2.0, len, t, c);
        RenderUtil.rect(ctx, cx - t / 2.0, cy - g - len, t, len, c);
        RenderUtil.rect(ctx, cx - t / 2.0, cy + g, t, len, c);
        if (dot.get()) {
            RenderUtil.rect(ctx, cx - t / 2.0, cy - t / 2.0, t, t, c);
        }
    }
}
