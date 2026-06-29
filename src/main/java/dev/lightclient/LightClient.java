package dev.lightclient;

import dev.lightclient.event.EventBus;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.gui.ClickGuiScreen;
import dev.lightclient.manager.CommandManager;
import dev.lightclient.manager.ConfigManager;
import dev.lightclient.manager.FriendManager;
import dev.lightclient.manager.HudManager;
import dev.lightclient.manager.ModuleManager;
import dev.lightclient.manager.NotificationManager;
import dev.lightclient.manager.RenderManager;
import dev.lightclient.manager.ThemeManager;
import dev.lightclient.module.Module;
import dev.lightclient.util.ClickTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point and service locator for the Light Client. Holds every manager
 * and wires the Fabric event lifecycle into the client's internal systems.
 */
public final class LightClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.NAME);

    private static LightClient instance;

    private final EventBus eventBus = new EventBus();
    private final ClickTracker clickTracker = new ClickTracker();

    private ThemeManager themeManager;
    private ModuleManager moduleManager;
    private HudManager hudManager;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private NotificationManager notificationManager;
    private RenderManager renderManager;
    private FriendManager friendManager;

    private KeyBinding clickGuiKey;

    public static LightClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("Initializing {} v{} for Minecraft {}", Reference.NAME, Reference.VERSION, Reference.GAME_VERSION);

        themeManager = new ThemeManager();
        hudManager = new HudManager();
        friendManager = new FriendManager();
        notificationManager = new NotificationManager();
        renderManager = new RenderManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        configManager = new ConfigManager();

        moduleManager.init();
        commandManager.init();
        renderManager.register();
        registerKeybinds();
        registerLifecycle();

        configManager.load();
        notificationManager.success(Reference.NAME, "v" + Reference.VERSION + " loaded");
        LOGGER.info("{} initialized with {} modules", Reference.NAME, moduleManager.getModules().size());
    }

    private void registerKeybinds() {
        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lightclient.clickgui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.create(net.minecraft.util.Identifier.of(Reference.MOD_ID, "main"))));
    }

    private void registerLifecycle() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> onClientTick(client));
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> !commandManager.handle(message));
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> configManager.save());
    }

    private void onClientTick(MinecraftClient client) {
        while (clickGuiKey.wasPressed()) {
            client.setScreen(new ClickGuiScreen());
        }
        if (client.currentScreen == null && client.player != null) {
            handleModuleKeybinds(client);
        }
        eventBus.post(TickEvent.INSTANCE);
    }

    private void handleModuleKeybinds(MinecraftClient client) {
        long handle = client.getWindow().getHandle();
        for (Module module : moduleManager.getModules()) {
            int key = module.getKey();
            if (key == GLFW.GLFW_KEY_UNKNOWN) {
                continue;
            }
            boolean pressed = GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS;
            boolean previous = pressedState.getOrDefault(module, false);
            if (pressed && !previous) {
                module.toggle();
            }
            pressedState.put(module, pressed);
        }
    }

    private final java.util.Map<Module, Boolean> pressedState = new java.util.HashMap<>();

    public void tickClickTracker() {
        clickTracker.update();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public HudManager getHudManager() {
        return hudManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public ClickTracker getClickTracker() {
        return clickTracker;
    }

    public KeyBinding getClickGuiKey() {
        return clickGuiKey;
    }
}
