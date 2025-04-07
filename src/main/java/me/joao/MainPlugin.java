package me.joao;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Loading Plugin");
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Enabling Plugin");
        getServer().getWorld("world").setBlockData();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Disabling Plugin");
    }
}
