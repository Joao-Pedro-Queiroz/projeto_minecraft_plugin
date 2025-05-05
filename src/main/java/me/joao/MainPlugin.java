package me.joao;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import me.joao.commands.CommandGameMode;
import me.joao.commands.CommandManager;
import me.joao.profiles.ProfileManager;
import me.joao.listener.LordListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public class MainPlugin extends JavaPlugin {

    @Getter
    private static MainPlugin instance;

    private ProfileManager profileManager;

    private CommandManager commandManager;

    private BukkitAudiences adventureManager;

    @Override
    public void onLoad() {
        instance = this;

        adventureManager = BukkitAudiences.create(instance);
        profileManager = new ProfileManager();
        commandManager = new CommandManager();
    }

    @Override
    public void onDisable() {
        commandManager.disable();
    }

    @Override
    public void onEnable() {
        registerListeners();
        createCommands();
    }

    private void registerListeners() {
        registerListener(profileManager);
        registerListener(commandManager);
        registerListener(new LordListener(this));
    }

    private void createCommands() {
        new CommandGameMode().register();
    }

    public void registerListener(@NotNull Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
