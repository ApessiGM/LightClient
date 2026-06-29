package dev.lightclient.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.lightclient.LightClient;
import dev.lightclient.Reference;
import dev.lightclient.account.OfflineAccount;
import dev.lightclient.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stores offline accounts and swaps the active client {@link Session} in-game so
 * the player can switch identity without restarting Minecraft.
 */
public final class AccountManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final List<OfflineAccount> accounts = new ArrayList<>();
    private final Path file = MinecraftClient.getInstance().runDirectory.toPath()
            .resolve("config").resolve(Reference.MOD_ID).resolve("accounts.json");

    public List<OfflineAccount> getAccounts() {
        return accounts;
    }

    public boolean add(String username) {
        if (username == null) {
            return false;
        }
        String trimmed = username.trim();
        if (trimmed.length() < 3 || trimmed.length() > 16 || !trimmed.matches("[A-Za-z0-9_]+")) {
            return false;
        }
        OfflineAccount account = new OfflineAccount(trimmed);
        if (accounts.contains(account)) {
            return false;
        }
        accounts.add(account);
        save();
        return true;
    }

    public void remove(OfflineAccount account) {
        accounts.remove(account);
        save();
    }

    /** Swaps the running client session to the given offline account. */
    public boolean login(OfflineAccount account) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Session session = new Session(
                account.getUsername(),
                account.getUuid(),
                "0",
                Optional.empty(),
                Optional.empty());
        try {
            ((MinecraftClientAccessor) mc).lightclient$setSession(session);
        } catch (Throwable throwable) {
            LightClient.LOGGER.error("Failed to switch session", throwable);
            return false;
        }
        LightClient.getInstance().getNotificationManager()
                .success("Account", "Logged in as " + account.getUsername());
        return true;
    }

    public String currentUsername() {
        return MinecraftClient.getInstance().getSession().getUsername();
    }

    public void load() {
        accounts.clear();
        try {
            if (!Files.exists(file)) {
                return;
            }
            String json = Files.readString(file, StandardCharsets.UTF_8);
            JsonObject root = GSON.fromJson(json, JsonObject.class);
            if (root == null || !root.has("accounts")) {
                return;
            }
            JsonArray array = root.getAsJsonArray("accounts");
            for (int i = 0; i < array.size(); i++) {
                String name = array.get(i).getAsString();
                OfflineAccount account = new OfflineAccount(name);
                if (!accounts.contains(account)) {
                    accounts.add(account);
                }
            }
        } catch (IOException | RuntimeException e) {
            LightClient.LOGGER.error("Failed to load accounts", e);
        }
    }

    public void save() {
        try {
            Files.createDirectories(file.getParent());
            JsonObject root = new JsonObject();
            JsonArray array = new JsonArray();
            for (OfflineAccount account : accounts) {
                array.add(account.getUsername());
            }
            root.add("accounts", array);
            Files.writeString(file, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LightClient.LOGGER.error("Failed to save accounts", e);
        }
    }
}
