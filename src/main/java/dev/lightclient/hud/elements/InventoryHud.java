package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

/** Renders the player's main inventory (slots 9-35) as a compact grid. */
public final class InventoryHud extends HudModule {
    private static final int SLOT = 18;
    private static final int COLUMNS = 9;
    private static final int ROWS = 3;

    public InventoryHud() {
        super("Inventory HUD", "Shows your inventory contents", 10, 150);
        setSize(COLUMNS * SLOT + 4, ROWS * SLOT + 4);
    }

    @Override
    public void render(DrawContext context) {
        setSize(COLUMNS * SLOT + 4, ROWS * SLOT + 4);
        if (mc.player == null) {
            return;
        }
        RenderUtil.roundedRect(context, getX(), getY(), COLUMNS * SLOT + 4, ROWS * SLOT + 4, 4,
                getBackgroundColor(0x121212));
        var stacks = mc.player.getInventory().getMainStacks();
        for (int i = 0; i < COLUMNS * ROWS; i++) {
            int slotIndex = i + 9;
            if (slotIndex >= stacks.size()) {
                break;
            }
            ItemStack stack = stacks.get(slotIndex);
            if (stack.isEmpty()) {
                continue;
            }
            int col = i % COLUMNS;
            int rowIndex = i / COLUMNS;
            int x = (int) getX() + 2 + col * SLOT + 1;
            int y = (int) getY() + 2 + rowIndex * SLOT + 1;
            context.drawItem(stack, x, y);
            context.drawStackOverlay(mc.textRenderer, stack, x, y);
        }
    }
}
