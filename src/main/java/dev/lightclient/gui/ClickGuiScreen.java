package dev.lightclient.gui;

import dev.lightclient.LightClient;
import dev.lightclient.Reference;
import dev.lightclient.gui.clickgui.Panel;
import dev.lightclient.module.Category;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/** The main ClickGUI: draggable category panels, search bar and theming. */
public final class ClickGuiScreen extends Screen {
    private final List<Panel> panels = new ArrayList<>();
    private String search = "";
    private boolean searchFocused;

    public ClickGuiScreen() {
        super(Text.literal(Reference.NAME));
    }

    @Override
    protected void init() {
        panels.clear();
        Category[] categories = Category.values();
        double startX = 12;
        double startY = 34;
        double spacing = 122;
        int perRow = Math.max(1, (int) ((width - startX) / spacing));
        for (int i = 0; i < categories.length; i++) {
            double px = startX + (i % perRow) * spacing;
            double py = startY + (i / perRow) * 250;
            panels.add(new Panel(categories[i], px, py));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(0, 0, width, height, ColorUtil.withAlpha(Reference.COLOR_DARK_SECONDARY, 140));

        int accent = LightClient.getInstance().getThemeManager().accent();
        // Title bar.
        RenderUtil.gradientH(context, 0, 0, width, 22, ColorUtil.withAlpha(Reference.COLOR_DARK_PRIMARY, 235),
                ColorUtil.withAlpha(Reference.COLOR_DARK_SECONDARY, 235));
        RenderUtil.text(context, "§lLight§r Client §7v" + Reference.VERSION, 12, 7, 0xFFFFFFFF);
        RenderUtil.rect(context, 0, 22, width, 1, accent);

        // Search bar.
        double sbX = width - 170;
        RenderUtil.roundedRect(context, sbX, 4, 158, 14, 3, ColorUtil.withAlpha(0x000000, 150));
        if (searchFocused) {
            RenderUtil.outline(context, sbX, 4, 158, 14, accent);
        }
        String shown = search.isEmpty() && !searchFocused ? "Search modules..." : search + (searchFocused ? "_" : "");
        RenderUtil.text(context, shown, sbX + 5, 7, search.isEmpty() && !searchFocused ? 0xFF6A6A72 : 0xFFFFFFFF);

        for (Panel panel : panels) {
            panel.setSearch(search);
            panel.render(context, mouseX, mouseY);
        }

        RenderUtil.text(context, "§7Right-click a module for settings  •  RShift to close",
                12, height - 12, 0xFFB9B9C0);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mx = click.x();
        double my = click.y();
        int button = click.button();
        if (my <= 18 && mx >= width - 170 && mx <= width - 12) {
            searchFocused = true;
            return true;
        }
        searchFocused = false;
        for (Panel panel : panels) {
            if (panel.mouseClicked(mx, my, button)) {
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        for (Panel panel : panels) {
            panel.mouseReleased(click.x(), click.y(), click.button());
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        for (Panel panel : panels) {
            panel.mouseDragged(click.x(), click.y());
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (Panel panel : panels) {
            panel.scroll(verticalAmount);
        }
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        int key = input.key();
        if (searchFocused) {
            if (key == GLFW.GLFW_KEY_BACKSPACE && !search.isEmpty()) {
                search = search.substring(0, search.length() - 1);
                return true;
            }
            if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_ESCAPE) {
                searchFocused = false;
                return true;
            }
        }
        for (Panel panel : panels) {
            if (panel.keyPressed(key)) {
                return true;
            }
        }
        if (key == GLFW.GLFW_KEY_ESCAPE || key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (searchFocused) {
            search += input.asString();
            return true;
        }
        return super.charTyped(input);
    }

    @Override
    public void close() {
        LightClient.getInstance().getConfigManager().save();
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
