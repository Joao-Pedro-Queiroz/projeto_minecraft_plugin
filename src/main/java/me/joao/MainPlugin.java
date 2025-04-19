package me.joao;

import lombok.Getter;
import me.joao.profiles.ProfileManager;
import me.joao.listener.LordListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainPlugin extends JavaPlugin {

    private ProfileManager profileManager;

    @Override
    public void onLoad() {
        getServer().getPluginManager().registerEvents(profileManager = new ProfileManager(), this);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new LordListener(this), this);
    }

    @Override
    public void onDisable() {
        //pass
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        return false;
    }
}
