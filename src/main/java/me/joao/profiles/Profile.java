package me.joao.profiles;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Profile {

    private final UUID uuid;

    public Profile(@NotNull UUID uuid) {
        this.uuid = uuid;
    }


    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return org.bukkit.Bukkit.getPlayer(getUUID());
    }

    @Nullable
    public OfflinePlayer getOfflinePlayer() {
        return org.bukkit.Bukkit.getOfflinePlayer(getUUID());
    }

    @NotNull
    public String getName() {
        OfflinePlayer player = getOfflinePlayer();

        if (player == null) {
            String playerName = player.getName();

            if (playerName != null) {
                return playerName;
            }
        }
        return "Unnamed";
    }

    public void sendMessage(@NotNull String message) {
        Player player = getPlayer();

        if (player != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
