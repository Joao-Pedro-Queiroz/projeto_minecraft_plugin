package me.joao.listener;

import me.joao.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LordListener implements Listener {

    private final Map<UUID, Profile> onlineProfiles = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Profile profile = new Profile(event.getPlayer().getUniqueId());

        onlineProfiles.put(profile.getUUID(), profile);

        profile.sendMessage("&aWelcome to the server, &6" + profile.getName() + "&a!");
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Profile profile = onlineProfiles.remove(event.getPlayer().getUniqueId());

        if (profile != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', '&' + profile.getName() + "&has left the server"));
        }
    }
}
