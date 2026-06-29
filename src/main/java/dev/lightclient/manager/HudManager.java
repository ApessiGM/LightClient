package dev.lightclient.manager;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Owns every {@link HudModule} and renders the enabled ones each frame,
 * applying their individual scale around their anchor point.
 */
public final class HudManager {
    private final List<HudModule> elements = new ArrayList<>();

    public void add(HudModule element) {
        if (!elements.contains(element)) {
            elements.add(element);
        }
    }

    public List<HudModule> getElements() {
        return elements;
    }

    public void render(DrawContext context) {
        for (HudModule element : elements) {
            if (!element.isEnabled()) {
                continue;
            }
            renderScaled(context, element);
        }
    }

    private void renderScaled(DrawContext context, HudModule element) {
        float scale = (float) element.getScale();
        float x = (float) element.getX();
        float y = (float) element.getY();
        Matrix3x2fStack matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(x, y);
        matrices.scale(scale, scale);
        matrices.translate(-x, -y);
        element.render(context);
        matrices.popMatrix();
    }

    /** Returns the topmost element whose scaled bounds contain the point. */
    public HudModule getElementAt(double mouseX, double mouseY) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            HudModule element = elements.get(i);
            if (!element.isEnabled()) {
                continue;
            }
            double x = element.getX();
            double y = element.getY();
            if (mouseX >= x && mouseX <= x + element.getWidth()
                    && mouseY >= y && mouseY <= y + element.getHeight()) {
                return element;
            }
        }
        return null;
    }
}
