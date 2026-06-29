package dev.lightclient.gui.clickgui.component;

import dev.lightclient.LightClient;
import dev.lightclient.setting.BooleanSetting;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

/** A checkbox-style toggle for a {@link BooleanSetting}. */
public final class BooleanComponent extends Component {
    private final BooleanSetting bool;

    public BooleanComponent(BooleanSetting setting) {
        super(setting);
        this.bool = setting;
        this.height = 14;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        int accent = LightClient.getInstance().getThemeManager().accent();
        RenderUtil.text(context, bool.getName(), x + 4, y + 3, 0xFFD8D8DF);
        double boxSize = 8;
        double boxX = x + width - boxSize - 5;
        double boxY = y + 3;
        RenderUtil.roundedRect(context, boxX, boxY, boxSize, boxSize, 2, 0xFF2A2A33);
        if (bool.get()) {
            RenderUtil.roundedRect(context, boxX, boxY, boxSize, boxSize, 2, accent);
        }
        if (hovered(mouseX, mouseY)) {
            RenderUtil.outline(context, x + 1, y, width - 2, height, ColorUtil.withAlpha(accent, 60));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && hovered(mouseX, mouseY)) {
            bool.toggle();
            return true;
        }
        return false;
    }
}
