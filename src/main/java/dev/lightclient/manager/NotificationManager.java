package dev.lightclient.manager;

import dev.lightclient.LightClient;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.animation.Animation;
import dev.lightclient.util.animation.Easing;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Renders transient toast notifications in the bottom-right corner. */
public final class NotificationManager {

    public enum Type {
        INFO(0xFF7B2DFF),
        SUCCESS(0xFF32D74B),
        WARNING(0xFFFFD60A),
        ERROR(0xFFFF453A);

        public final int color;

        Type(int color) {
            this.color = color;
        }
    }

    private static final class Notification {
        final String title;
        final String message;
        final Type type;
        final long created;
        final long lifetime;
        final Animation animation;

        Notification(String title, String message, Type type, long lifetime) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.created = System.currentTimeMillis();
            this.lifetime = lifetime;
            this.animation = new Animation(220, Easing.EASE_OUT_EXPO);
            this.animation.animateTo(1.0);
        }

        boolean isExpired() {
            return System.currentTimeMillis() - created > lifetime;
        }
    }

    private final List<Notification> notifications = new CopyOnWriteArrayList<>();

    public void push(String title, String message, Type type) {
        push(title, message, type, 3500);
    }

    public void push(String title, String message, Type type, long lifetime) {
        notifications.add(new Notification(title, message, type, lifetime));
    }

    public void info(String title, String message) {
        push(title, message, Type.INFO);
    }

    public void success(String title, String message) {
        push(title, message, Type.SUCCESS);
    }

    public void error(String title, String message) {
        push(title, message, Type.ERROR);
    }

    public void render(DrawContext context) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        int width = 150;
        int height = 30;
        int margin = 6;

        int index = 0;
        for (Iterator<Notification> it = notifications.iterator(); it.hasNext(); ) {
            Notification notification = it.next();
            if (notification.isExpired()) {
                notifications.remove(notification);
                continue;
            }
            float anim = notification.animation.getFloat();
            double offset = (1.0 - anim) * (width + margin);
            double x = screenWidth - width - margin + offset;
            double y = screenHeight - (height + margin) * (index + 1) - margin;

            int bg = ColorUtil.withAlpha(0x121212, (int) (220 * anim));
            RenderUtil.roundedRect(context, x, y, width, height, 4, bg);
            RenderUtil.rect(context, x, y, 3, height, ColorUtil.multiplyAlpha(notification.type.color, anim));
            RenderUtil.textShadow(context, notification.title, x + 8, y + 5,
                    ColorUtil.multiplyAlpha(0xFFFFFFFF, anim));
            RenderUtil.textShadow(context, notification.message, x + 8, y + 16,
                    ColorUtil.multiplyAlpha(0xFFB9B9C0, anim));
            index++;
        }
    }

    public static NotificationManager get() {
        return LightClient.getInstance().getNotificationManager();
    }
}
