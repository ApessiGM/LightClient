package dev.lightclient.util.render;

import dev.lightclient.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

/**
 * Drawing helpers built on top of vanilla's {@link DrawContext}. Everything is
 * expressed through the public, version stable {@code fill}/{@code drawText}
 * primitives so the client stays resilient across Minecraft updates.
 */
public final class RenderUtil {
    private RenderUtil() {
    }

    private static TextRenderer font() {
        return MinecraftClient.getInstance().textRenderer;
    }

    public static void rect(DrawContext ctx, double x, double y, double width, double height, int color) {
        ctx.fill((int) x, (int) y, (int) (x + width), (int) (y + height), color);
    }

    /** A rectangle with the four hard corners trimmed to fake rounded edges. */
    public static void roundedRect(DrawContext ctx, double x, double y, double width, double height,
                                   double radius, int color) {
        int x1 = (int) x;
        int y1 = (int) y;
        int x2 = (int) (x + width);
        int y2 = (int) (y + height);
        int r = (int) Math.max(0, Math.min(radius, Math.min(width, height) / 2));
        // Centre block + side blocks leave the corners empty.
        ctx.fill(x1 + r, y1, x2 - r, y2, color);
        ctx.fill(x1, y1 + r, x1 + r, y2 - r, color);
        ctx.fill(x2 - r, y1 + r, x2, y2 - r, color);
        // Soften corners with a couple of stepped insets.
        for (int i = 0; i < r; i++) {
            int inset = (int) Math.round(r - Math.sqrt(r * r - (r - i) * (r - i)));
            ctx.fill(x1 + inset, y1 + i, x2 - inset, y1 + i + 1, color);
            ctx.fill(x1 + inset, y2 - i - 1, x2 - inset, y2 - i, color);
        }
    }

    /** Vertical gradient fill. */
    public static void gradientV(DrawContext ctx, double x, double y, double width, double height,
                                 int top, int bottom) {
        int steps = (int) Math.max(1, height);
        for (int i = 0; i < steps; i++) {
            float t = steps <= 1 ? 0f : (float) i / (steps - 1);
            int color = ColorUtil.lerp(top, bottom, t);
            ctx.fill((int) x, (int) (y + i), (int) (x + width), (int) (y + i + 1), color);
        }
    }

    /** Horizontal gradient fill. */
    public static void gradientH(DrawContext ctx, double x, double y, double width, double height,
                                 int left, int right) {
        int steps = (int) Math.max(1, width);
        for (int i = 0; i < steps; i++) {
            float t = steps <= 1 ? 0f : (float) i / (steps - 1);
            int color = ColorUtil.lerp(left, right, t);
            ctx.fill((int) (x + i), (int) y, (int) (x + i + 1), (int) (y + height), color);
        }
    }

    /** A soft outer glow drawn as expanding translucent frames. */
    public static void glow(DrawContext ctx, double x, double y, double width, double height,
                            int color, int spread) {
        for (int i = spread; i >= 1; i--) {
            int alpha = (int) (ColorUtil.alpha(color) * (0.10f * (spread - i + 1) / spread));
            int c = ColorUtil.multiplyAlpha(color, alpha / 255f);
            ctx.fill((int) (x - i), (int) (y - i), (int) (x + width + i), (int) (y + height + i), c);
        }
    }

    /** Thin outline. */
    public static void outline(DrawContext ctx, double x, double y, double width, double height, int color) {
        int x1 = (int) x;
        int y1 = (int) y;
        int x2 = (int) (x + width);
        int y2 = (int) (y + height);
        ctx.fill(x1, y1, x2, y1 + 1, color);
        ctx.fill(x1, y2 - 1, x2, y2, color);
        ctx.fill(x1, y1, x1 + 1, y2, color);
        ctx.fill(x2 - 1, y1, x2, y2, color);
    }

    public static void text(DrawContext ctx, String text, double x, double y, int color) {
        ctx.drawText(font(), text, (int) x, (int) y, color, false);
    }

    public static void textShadow(DrawContext ctx, String text, double x, double y, int color) {
        ctx.drawText(font(), text, (int) x, (int) y, color, true);
    }

    public static void textCentered(DrawContext ctx, String text, double centerX, double y, int color) {
        double x = centerX - textWidth(text) / 2.0;
        ctx.drawText(font(), text, (int) x, (int) y, color, true);
    }

    public static int textWidth(String text) {
        return font().getWidth(text);
    }

    public static int fontHeight() {
        return font().fontHeight;
    }
}
