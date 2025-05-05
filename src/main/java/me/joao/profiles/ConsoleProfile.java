package me.joao.profiles;

import lombok.*;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import me.joao.MainPlugin;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleProfile implements Profile {

    protected static final ConsoleProfile INSTANCE = new ConsoleProfile();

    @NotNull
    @NonNull
    private String name;

    @Override
    public @Nullable UUID getUUID() {
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return true;
    }

    @Override
    public void setGameMode(@NotNull GameMode mode) {}

    @Override
    public void sendMessage(@NotNull Component component) {
        MainPlugin.getInstance().getAdventureManager().sender(getAsCommandSender())
                .sendMessage(component);
    }

    @NotNull
    private ConsoleCommandSender getAsCommandSender() {
        return MainPlugin.getInstance().getServer().getConsoleSender();
    }
}
