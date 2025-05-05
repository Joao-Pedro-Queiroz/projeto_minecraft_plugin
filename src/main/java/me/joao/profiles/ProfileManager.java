package me.joao.profiles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.joao.MainPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class ProfileManager implements Listener {

    @Getter
    private final MainPlugin plugin = MainPlugin.getInstance();

    private final Map<UUID, PlayerProfile> onlineProfiles = new ConcurrentHashMap<>();

    public Profile getConsole() {
        return ConsoleProfile.INSTANCE;
    }

    @NotNull
    public Profile getProfile(@NotNull CommandSender sender) {
        if (sender instanceof Player player) {
            return onlineProfiles.get(player.getUniqueId());
        }
        if (sender instanceof ConsoleCommandSender) {
            return getConsole();
        }
        throw new UnsupportedOperationException(
                "Unsupported CommandSender type: " + sender.getClass().getName());
    }

    public Profile getProfile(@NotNull UUID uuid) {
        return onlineProfiles.get(uuid);
    }

    public List<String> getOnlinePlayerNames(@Nullable String filter) {
        if (filter == null) {
            return onlineProfiles.values().stream().map(PlayerProfile::getName).toList();
        }
        return onlineProfiles.values().stream().map(PlayerProfile::getName)
                .filter(name -> name.toLowerCase().startsWith(filter.toLowerCase())).toList();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        PlayerProfile profile = new PlayerProfile(this, event.getPlayer().getUniqueId(),
                event.getPlayer().getName());

        onlineProfiles.put(profile.getUUID(), profile);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        onlineProfiles.remove(event.getPlayer().getUniqueId());
    }

    @Nullable
    public Profile getProfile(@NotNull String profileName) {
        for (PlayerProfile profile : onlineProfiles.values()) {
            if (profile.getName().equalsIgnoreCase(profileName)) {
                return profile;
            }
        }
        return null;
    }
}