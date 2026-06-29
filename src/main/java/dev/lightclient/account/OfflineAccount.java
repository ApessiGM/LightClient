package dev.lightclient.account;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * A crackable / offline account identified solely by its username. The UUID is
 * derived the same way a vanilla offline server generates it, so the player
 * keeps a stable identity across sessions.
 */
public final class OfflineAccount {
    private final String username;

    public OfflineAccount(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OfflineAccount other && other.username.equalsIgnoreCase(username);
    }

    @Override
    public int hashCode() {
        return username.toLowerCase().hashCode();
    }
}
