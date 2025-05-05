package me.joao.profiles;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class PlayerProfile implements Profile {

    @NotNull
    @NonNull
    private final ProfileManager manager;

    @NotNull
    @NonNull
    private final UUID uuid;

    @NotNull
    @NonNull
    private String name;

    @Override
    public @Nullable UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return org.bukkit.Bukkit.getPlayer(uuid);
    }

    @Nullable
    public OfflinePlayer getOfflinePlayer() {
        return org.bukkit.Bukkit.getOfflinePlayer(uuid);
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

    @Override
    public void sendMessage(@NotNull Component component) {
        Player player = getPlayer();

        if (player != null) {
            manager.getPlugin().getAdventureManager().sender(player).sendMessage(component);
        }
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

    @Override
    public void setGameMode(@NotNull GameMode mode) {
        Player player = getPlayer();

        if (player != null) {
            player.setGameMode(mode);
        }
    }
}