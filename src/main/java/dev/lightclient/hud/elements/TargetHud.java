package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import dev.lightclient.util.MathUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/** Shows information about the entity currently under the crosshair. */
public final class TargetHud extends HudModule {
    public TargetHud() {
        super("Target HUD", "Info about your target", 150, 90);
    }

    @Override
    public void render(DrawContext context) {
        LivingEntity target = resolveTarget();
        if (target == null) {
            setSize(0, 0);
            return;
        }
        double width = 120;
        double height = 34;
        setSize(width, height);
        RenderUtil.roundedRect(context, getX(), getY(), width, height, 4, getBackgroundColor(0x121212));
        RenderUtil.textShadow(context, target.getName().getString(), getX() + 6, getY() + 5, 0xFFFFFFFF);

        float ratio = MathUtil.clamp(target.getHealth() / target.getMaxHealth(), 0f, 1f);
        double barWidth = width - 12;
        RenderUtil.roundedRect(context, getX() + 6, getY() + 18, barWidth, 6, 2, 0xFF2A2A2A);
        RenderUtil.roundedRect(context, getX() + 6, getY() + 18, barWidth * ratio, 6, 2, accent());
        RenderUtil.textShadow(context, String.format("%.1f HP", target.getHealth()), getX() + 6, getY() + 25,
                0xFFB9B9C0);
    }

    private LivingEntity resolveTarget() {
        if (mc.crosshairTarget instanceof EntityHitResult hit && hit.getType() == HitResult.Type.ENTITY) {
            Entity entity = hit.getEntity();
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }
}
