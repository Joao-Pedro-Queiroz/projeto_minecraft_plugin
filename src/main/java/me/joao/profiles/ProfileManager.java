package me.joao.profiles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager implements Listener {

    private final Map<UUID, Profile> onlineProfiles = new ConcurrentHashMap<>();

    public Profile getProfile(@NotNull UUID uuid) {
        return onlineProfiles.computeIfAbsent(uuid, Profile::new);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Profile profile = new Profile(event.getPlayer().getUniqueId());

        onlineProfiles.put(profile.getID(), profile);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        onlineProfiles.remove(event.getPlayer().getUniqueId());
    }
}