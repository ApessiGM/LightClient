package dev.lightclient.gui.clickgui;

import dev.lightclient.LightClient;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/** A draggable category panel listing its modules with a glassmorphism style. */
public final class Panel {
    private static final double WIDTH = 112;
    private static final double HEADER = 16;
    private static final double MAX_BODY = 220;

    private final Category category;
    private final List<ModuleButton> buttons = new ArrayList<>();

    private double x;
    private double y;
    private boolean open = true;
    private boolean dragging;
    private double dragX;
    private double dragY;
    private double scroll;
    private String search = "";

    public Panel(Category category, double x, double y) {
        this.category = category;
        this.x = x;
        this.y = y;
        for (Module module : LightClient.getInstance().getModuleManager().getModules(category)) {
            buttons.add(new ModuleButton(module));
        }
    }

    public void setSearch(String search) {
        this.search = search == null ? "" : search.toLowerCase();
    }

    private boolean matches(ModuleButton button) {
        return search.isEmpty() || button.getModule().getName().toLowerCase().contains(search);
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        int accent = LightClient.getInstance().getThemeManager().accent();
        int accentEnd = LightClient.getInstance().getThemeManager().accentEnd();

        // Glow + header.
        RenderUtil.glow(context, x, y, WIDTH, HEADER, accent, 4);
        RenderUtil.gradientH(context, x, y, WIDTH, HEADER, accent, accentEnd);
        RenderUtil.text(context, category.getDisplayName(), x + 6, y + 4, 0xFF121212);
        RenderUtil.text(context, open ? "v" : ">", x + WIDTH - 10, y + 4, 0xFF121212);

        if (!open) {
            return;
        }

        double bodyHeight = Math.min(MAX_BODY, totalHeight());
        // Glassmorphism body: translucent dark surface.
        RenderUtil.rect(context, x, y + HEADER, WIDTH, bodyHeight, ColorUtil.withAlpha(0x0A0A0A, 215));
        RenderUtil.outline(context, x, y, WIDTH, HEADER + bodyHeight, ColorUtil.withAlpha(accent, 70));

        context.enableScissor((int) x, (int) (y + HEADER), (int) (x + WIDTH), (int) (y + HEADER + bodyHeight));
        double cy = y + HEADER + 2 - scroll;
        for (ModuleButton button : buttons) {
            if (!matches(button)) {
                continue;
            }
            button.setBounds(x + 2, cy, WIDTH - 4);
            button.render(context, mouseX, mouseY);
            cy += button.getTotalHeight() + 1;
        }
        context.disableScissor();
    }

    private double totalHeight() {
        double total = 4;
        for (ModuleButton button : buttons) {
            if (matches(button)) {
                total += button.getTotalHeight() + 1;
            }
        }
        return total;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (inHeader(mouseX, mouseY)) {
            if (button == 0) {
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            } else if (button == 1) {
                open = !open;
            }
            return true;
        }
        if (!open || !inBody(mouseX, mouseY)) {
            return false;
        }
        for (ModuleButton moduleButton : buttons) {
            if (matches(moduleButton) && moduleButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        for (ModuleButton moduleButton : buttons) {
            moduleButton.mouseReleased(mouseX, mouseY, button);
        }
    }

    public void mouseDragged(double mouseX, double mouseY) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    public boolean keyPressed(int keyCode) {
        for (ModuleButton moduleButton : buttons) {
            if (moduleButton.keyPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }

    public void scroll(double amount) {
        double bodyHeight = Math.min(MAX_BODY, totalHeight());
        double max = Math.max(0, totalHeight() - bodyHeight);
        scroll = Math.max(0, Math.min(max, scroll - amount * 12));
    }

    private boolean inHeader(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + WIDTH && mouseY >= y && mouseY <= y + HEADER;
    }

    private boolean inBody(double mouseX, double mouseY) {
        double bodyHeight = Math.min(MAX_BODY, totalHeight());
        return mouseX >= x && mouseX <= x + WIDTH && mouseY >= y + HEADER && mouseY <= y + HEADER + bodyHeight;
    }
}
