package dev.lightclient.gui.clickgui.component;

import dev.lightclient.LightClient;
import dev.lightclient.setting.NumberSetting;
import dev.lightclient.util.MathUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

/** A draggable slider for a {@link NumberSetting}. */
public final class NumberComponent extends Component {
    private final NumberSetting number;
    private boolean dragging;

    public NumberComponent(NumberSetting setting) {
        super(setting);
        this.number = setting;
        this.height = 22;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        int accent = LightClient.getInstance().getThemeManager().accent();
        String value = formatValue();
        RenderUtil.text(context, number.getName(), x + 4, y + 2, 0xFFD8D8DF);
        RenderUtil.text(context, value, x + width - RenderUtil.textWidth(value) - 5, y + 2, 0xFFB9B9C0);

        double trackX = x + 4;
        double trackY = y + 14;
        double trackWidth = width - 8;
        RenderUtil.roundedRect(context, trackX, trackY, trackWidth, 4, 2, 0xFF2A2A33);

        double progress = (number.get() - number.getMin()) / (number.getMax() - number.getMin());
        double fillWidth = trackWidth * MathUtil.clamp(progress, 0.0, 1.0);
        RenderUtil.roundedRect(context, trackX, trackY, fillWidth, 4, 2, accent);
        RenderUtil.roundedRect(context, trackX + fillWidth - 2, trackY - 2, 4, 8, 2, 0xFFFFFFFF);

        if (dragging) {
            updateValue(mouseX, trackX, trackWidth);
        }
    }

    private String formatValue() {
        if (number.getStep() >= 1) {
            return String.valueOf(number.getInt());
        }
        return String.format("%.2f", number.get());
    }

    private void updateValue(double mouseX, double trackX, double trackWidth) {
        double ratio = MathUtil.clamp((mouseX - trackX) / trackWidth, 0.0, 1.0);
        double value = number.getMin() + ratio * (number.getMax() - number.getMin());
        number.set(value);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && hovered(mouseX, mouseY) && mouseY >= y + 10) {
            dragging = true;
            updateValue(mouseX, x + 4, width - 8);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return false;
    }
}
