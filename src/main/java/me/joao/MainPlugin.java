package me.joao;

import me.joao.listener.LordListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        getServer().getConsoleSender().sendMessage("Loading InsperCodingPlugin...");
    }

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("Plugin is now enabled!");

        getServer().getPluginManager().registerEvents(new LordListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("Plugin is now disabled!");
    }
}
