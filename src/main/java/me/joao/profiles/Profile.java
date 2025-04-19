package me.joao.profiles;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Profile {

    private final UUID uuid;

    private int coins = 0;

    public Profile(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public UUID getID() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return org.bukkit.Bukkit.getPlayer(getID());
    }

    @Nullable
    public OfflinePlayer getOfflinePlayer() {
        return org.bukkit.Bukkit.getOfflinePlayer(getID());
    }

    @NotNull
    public String getName() {
        OfflinePlayer player = getOfflinePlayer();

        if (player != null) {
            String playerName = player.getName();

            if (playerName != null) {
                return playerName;
            }
        }
        return "Unknown";
    }

    public void sendMessage(@NotNull String message) {
        Player player = getPlayer();

        if (player != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void sendTitle(@NotNull String title) {
        sendTitle(title, "");
    }

    public void sendTitle(@NotNull String title, @NotNull String subtitle) {
        sendTitle(title, subtitle, 10, 70, 20);
    }

    public void sendTitle(@NotNull String title, @NotNull String subtitle, int fadeIn, int stay,
                          int fadeOut) {
        Player player = getPlayer();

        if (player == null) {
            return;
        }
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', title),
                ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut);
    }

    private User luckPermsUser() {
        LuckPerms luckPerms = LuckPermsProvider.get();

        User user = luckPerms.getUserManager().getUser(uuid);

        if (user == null) {
            return luckPerms.getUserManager().loadUser(uuid).join();
        }
        return user;
    }

    public void grantPermission(@NotNull String permission) {
        grantPermission(permission, false);
    }

    public void grantPermission(@NotNull String permission, boolean negated) {
        grantPermission(permission, negated, ImmutableContextSet.empty());
    }

    public void grantPermission(@NotNull String permission, boolean negated,
                                @NotNull ContextSet context) {

        User user = luckPermsUser();

        if (user == null) {
            return;
        }
        user.data().add(Node.builder(permission).context(context).value(!negated).build());

        LuckPermsProvider.get().getUserManager().saveUser(user);
    }

    public void revokePermission(@NotNull String permission) {
        revokePermission(permission, false);
    }

    public void revokePermission(@NotNull String permission, boolean negated) {
        revokePermission(permission, negated, ImmutableContextSet.empty());
    }

    public void revokePermission(@NotNull String permission, boolean negated,
                                 @NotNull ContextSet context) {

        User user = luckPermsUser();

        if (user == null) {
            return;
        }
        user.data().remove(Node.builder(permission).context(context).value(!negated).build());

        LuckPermsProvider.get().getUserManager().saveUser(user);
    }

    public boolean hasPermission(@NotNull String permission) {
        return hasPermission(permission, QueryOptions.defaultContextualOptions());
    }

    public boolean hasPermission(@NotNull String permission, @NotNull QueryOptions context) {
        User user = luckPermsUser();

        if (user == null) {
            return false;
        }
        return user.getCachedData().getPermissionData(context).checkPermission(permission)
                .asBoolean();
    }
}