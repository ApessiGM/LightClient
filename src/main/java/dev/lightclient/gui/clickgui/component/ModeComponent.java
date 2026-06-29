package dev.lightclient.gui.clickgui.component;

import dev.lightclient.LightClient;
import dev.lightclient.setting.ModeSetting;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

/** Cycles through the options of a {@link ModeSetting}. */
public final class ModeComponent extends Component {
    private final ModeSetting mode;

    public ModeComponent(ModeSetting setting) {
        super(setting);
        this.mode = setting;
        this.height = 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        int accent = LightClient.getInstance().getThemeManager().accent();
        RenderUtil.text(context, mode.getName(), x + 4, y + 3, 0xFFD8D8DF);
        String value = mode.get();
        RenderUtil.text(context, "< " + value + " >", x + width - RenderUtil.textWidth("< " + value + " >") - 5,
                y + 3, accent);
        if (hovered(mouseX, mouseY)) {
            RenderUtil.outline(context, x + 1, y, width - 2, height, ColorUtil.withAlpha(accent, 60));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!hovered(mouseX, mouseY)) {
            return false;
        }
        if (button == 0) {
            mode.cycle();
        } else if (button == 1) {
            mode.cycleBackwards();
        }
        return true;
    }
}
