package dev.lightclient.util;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Polls the raw GLFW mouse button state every frame to provide an accurate
 * clicks-per-second reading without relying on game tick rate.
 */
public final class ClickTracker {
    private final Deque<Long> leftClicks = new ArrayDeque<>();
    private final Deque<Long> rightClicks = new ArrayDeque<>();
    private boolean leftDown;
    private boolean rightDown;

    public void update() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getWindow() == null) {
            return;
        }
        long handle = mc.getWindow().getHandle();
        boolean left = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean right = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        long now = System.currentTimeMillis();
        if (left && !leftDown) {
            leftClicks.add(now);
        }
        if (right && !rightDown) {
            rightClicks.add(now);
        }
        leftDown = left;
        rightDown = right;
        prune(leftClicks, now);
        prune(rightClicks, now);
    }

    private void prune(Deque<Long> deque, long now) {
        while (!deque.isEmpty() && now - deque.peekFirst() > 1000L) {
            deque.pollFirst();
        }
    }

    public int getLeftCps() {
        return leftClicks.size();
    }

    public int getRightCps() {
        return rightClicks.size();
    }
}
