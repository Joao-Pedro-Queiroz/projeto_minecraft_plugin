package me.joao.profiles;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Profile {

    @Nullable UUID getUUID();

    default boolean isPlayer() {
        return getUUID() != null;
    }

    @NotNull String getName();

    @NotNull Profile setName(@NotNull String name);

    boolean hasPermission(@NotNull String permission);

    void sendMessage(@NotNull Component component);

    void setGameMode(@NotNull GameMode mode);
}