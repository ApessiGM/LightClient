package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

/** Displays the player's currently worn armour and held item. */
public final class ArmorHud extends HudModule {
    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND
    };

    public ArmorHud() {
        super("Armor HUD", "Shows worn armour", 220, 4);
    }

    @Override
    public void render(DrawContext context) {
        setSize(SLOTS.length * 18, 18);
        if (mc.player == null) {
            return;
        }
        RenderUtil.roundedRect(context, getX(), getY(), getWidthRaw(), 18, 3, getBackgroundColor(0x121212));
        int x = (int) getX() + 1;
        int y = (int) getY() + 1;
        for (EquipmentSlot slot : SLOTS) {
            ItemStack stack = mc.player.getEquippedStack(slot);
            if (!stack.isEmpty()) {
                context.drawItem(stack, x, y);
                context.drawStackOverlay(mc.textRenderer, stack, x, y);
            }
            x += 18;
        }
    }

    private double getWidthRaw() {
        return SLOTS.length * 18;
    }
}
