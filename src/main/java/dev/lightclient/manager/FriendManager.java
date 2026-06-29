package dev.lightclient.manager;

import java.util.LinkedHashSet;
import java.util.Set;

/** Tracks friend usernames used by combat/render targeting and the tab list. */
public final class FriendManager {
    private final Set<String> friends = new LinkedHashSet<>();

    public boolean add(String name) {
        return friends.add(name.toLowerCase());
    }

    public boolean remove(String name) {
        return friends.remove(name.toLowerCase());
    }

    public boolean isFriend(String name) {
        return name != null && friends.contains(name.toLowerCase());
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void clear() {
        friends.clear();
    }
}
