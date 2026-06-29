package dev.lightclient.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.lightclient.LightClient;
import dev.lightclient.Reference;
import dev.lightclient.hud.HudModule;
import dev.lightclient.module.Module;
import dev.lightclient.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Persists module state, settings, HUD layout, themes and friends to JSON.
 * Supports multiple named profiles plus import/export of standalone files.
 */
public final class ConfigManager {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path baseDir;
    private final Path profilesDir;

    public ConfigManager() {
        this.baseDir = FabricLoader.getInstance().getConfigDir().resolve(Reference.MOD_ID);
        this.profilesDir = baseDir.resolve("profiles");
        try {
            Files.createDirectories(profilesDir);
        } catch (IOException ignored) {
        }
    }

    private Path mainConfig() {
        return baseDir.resolve("config.json");
    }

    public void save() {
        write(mainConfig());
    }

    public void load() {
        read(mainConfig());
    }

    public void saveProfile(String name) {
        write(profilesDir.resolve(sanitize(name) + ".json"));
    }

    public boolean loadProfile(String name) {
        Path path = profilesDir.resolve(sanitize(name) + ".json");
        if (!Files.exists(path)) {
            return false;
        }
        read(path);
        return true;
    }

    public List<String> listProfiles() {
        List<String> names = new ArrayList<>();
        if (!Files.isDirectory(profilesDir)) {
            return names;
        }
        try (Stream<Path> stream = Files.list(profilesDir)) {
            stream.filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        String file = p.getFileName().toString();
                        names.add(file.substring(0, file.length() - ".json".length()));
                    });
        } catch (IOException ignored) {
        }
        return names;
    }

    public boolean export(Path target) {
        try {
            Files.createDirectories(target.toAbsolutePath().getParent());
            Files.writeString(target, serialize().toString());
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    public boolean importFrom(Path source) {
        if (!Files.exists(source)) {
            return false;
        }
        read(source);
        return true;
    }

    private void write(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, gson.toJson(serialize()));
        } catch (IOException ignored) {
        }
    }

    private void read(Path path) {
        try {
            if (!Files.exists(path)) {
                return;
            }
            JsonObject root = gson.fromJson(Files.readString(path), JsonObject.class);
            if (root != null) {
                deserialize(root);
            }
        } catch (IOException | RuntimeException ignored) {
        }
    }

    private JsonObject serialize() {
        LightClient client = LightClient.getInstance();
        JsonObject root = new JsonObject();
        root.addProperty("version", Reference.VERSION);
        root.addProperty("prefix", Reference.COMMAND_PREFIX);
        root.addProperty("theme", client.getThemeManager().getActive().name);

        JsonObject modules = new JsonObject();
        for (Module module : client.getModuleManager().getModules()) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enabled", module.isEnabled());
            moduleObject.addProperty("key", module.getKey());

            JsonObject settings = new JsonObject();
            for (Setting setting : module.getSettings()) {
                Object value = setting.serialize();
                if (value instanceof Boolean bool) {
                    settings.addProperty(setting.getName(), bool);
                } else if (value instanceof Number number) {
                    settings.addProperty(setting.getName(), number);
                } else {
                    settings.addProperty(setting.getName(), String.valueOf(value));
                }
            }
            moduleObject.add("settings", settings);

            if (module instanceof HudModule hud) {
                moduleObject.addProperty("x", hud.getX());
                moduleObject.addProperty("y", hud.getY());
            }
            modules.add(module.getName(), moduleObject);
        }
        root.add("modules", modules);

        JsonObject friends = new JsonObject();
        client.getFriendManager().getFriends().forEach(name -> friends.addProperty(name, true));
        root.add("friends", friends);
        return root;
    }

    private void deserialize(JsonObject root) {
        LightClient client = LightClient.getInstance();
        if (root.has("theme")) {
            client.getThemeManager().setActive(root.get("theme").getAsString());
        }
        if (root.has("modules")) {
            JsonObject modules = root.getAsJsonObject("modules");
            for (Module module : client.getModuleManager().getModules()) {
                if (!modules.has(module.getName())) {
                    continue;
                }
                JsonObject moduleObject = modules.getAsJsonObject(module.getName());
                if (moduleObject.has("key")) {
                    module.setKey(moduleObject.get("key").getAsInt());
                }
                if (moduleObject.has("settings")) {
                    JsonObject settings = moduleObject.getAsJsonObject("settings");
                    for (Setting setting : module.getSettings()) {
                        if (!settings.has(setting.getName())) {
                            continue;
                        }
                        applySetting(setting, settings, setting.getName());
                    }
                }
                if (module instanceof HudModule hud && moduleObject.has("x") && moduleObject.has("y")) {
                    hud.setPosition(moduleObject.get("x").getAsDouble(), moduleObject.get("y").getAsDouble());
                }
                if (moduleObject.has("enabled") && moduleObject.get("enabled").getAsBoolean()) {
                    module.setEnabled(true);
                }
            }
        }
        if (root.has("friends")) {
            client.getFriendManager().clear();
            root.getAsJsonObject("friends").keySet().forEach(client.getFriendManager()::add);
        }
    }

    private void applySetting(Setting setting, JsonObject settings, String key) {
        com.google.gson.JsonElement element = settings.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        com.google.gson.JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isBoolean()) {
            setting.deserialize(primitive.getAsBoolean());
        } else if (primitive.isNumber()) {
            setting.deserialize(primitive.getAsDouble());
        } else {
            setting.deserialize(primitive.getAsString());
        }
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9-_]", "_");
    }

    public Path getBaseDir() {
        return baseDir;
    }
}
