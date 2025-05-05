package me.joao.commands;

import me.joao.MainPlugin;
import me.joao.events.AsyncCommandEvent;
import me.joao.events.CodeCommandEvent;
import me.joao.profiles.Profile;
import me.joao.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class CommandManager implements Listener {
    
    /**
     * The "commandMap" instance of the CraftServer {@link Class}
     *
     * @since 1.11.11
     */
    
    private static CommandMap commandMap;
    
    /**
     * The "knownCommands" {@link Map} of the {@link CommandMap} {@link Class}
     *
     * @since 1.11.11
     */
    
    private static Map<String, org.bukkit.command.Command> knownCommands;
    
    static {
        try {
            Server server = Bukkit.getServer();
            
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            
            commandMap = (CommandMap) field.get(server);
            
            if (MinecraftVersion.isAtMost(MinecraftVersion.V1_12_R1)) {
                field = commandMap.getClass().getDeclaredField("knownCommands");
                field.setAccessible(true);
                
                knownCommands = (Map<String, org.bukkit.command.Command>) field.get(commandMap);
            }
            else {
                Method method = commandMap.getClass().getDeclaredMethod("getKnownCommands");
                method.setAccessible(true);
                
                knownCommands = (Map<String, org.bukkit.command.Command>) method.invoke(commandMap);
            }
        }
        catch (Exception e) {
            MainPlugin.getInstance().getLogger()
                    .log(Level.SEVERE, "CommandManager Initialization ERROR", e);
        }
    }
    
    /**
     * Removes a command label from the main {@link CommandMap} and the async map.
     *
     * @param label The command label to be removed.
     *
     * @since 1.0.0
     */
    
    public static void removeCommandFromCommandMap(@NotNull String label) {
        if (label.isBlank()) {
            return;
        }
        knownCommands.keySet().parallelStream()
                .filter(k -> label.equalsIgnoreCase(k.substring(k.indexOf(':') + 1)))
                .forEach(knownCommands::remove);
    }
    
    private final Map<String, Command> asyncCommands;
    
    private final Map<String, Command> syncCommands;
    
    public CommandManager() {
        asyncCommands = new ConcurrentHashMap<>();
        syncCommands = new ConcurrentHashMap<>();
    }
    
    void registerCommand(@NotNull Command command) {
        if (command.isAsync()) {
            command.getAllNames().forEach(label -> asyncCommands.put(label.toLowerCase(), command));
            return;
        }
        command.getAllNames().forEach(label -> syncCommands.put(label.toLowerCase(), command));
    }
    
    public void registerCommand(@NotNull String label, @NotNull Command command) {
        if (command.isAsync()) {
            asyncCommands.put(label.toLowerCase(), command);
            return;
        }
        syncCommands.put(label.toLowerCase(), command);
    }
    
    public boolean isRegistered(@NotNull Command command) {
        return asyncCommands.containsValue(command) || syncCommands.containsValue(command);
    }
    
    void unregisterCommand(@NotNull Command command) {
        command.getAllNames().forEach(label -> unregisterCommandAlias(label, command));
    }
    
    public void unregisterCommand(@NotNull String label) {
        asyncCommands.remove(label);
        syncCommands.remove(label);
    }
    
    void unregisterCommandAlias(@NotNull String label, @NotNull Command command) {
        asyncCommands.remove(label.toLowerCase(), command);
        syncCommands.remove(label.toLowerCase(), command);
    }
    
    public void disable() {
        asyncCommands.clear();
        syncCommands.clear();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        if (!event.getMessage().startsWith("/")) {
            return;
        }
        Profile sender = MainPlugin.getInstance().getProfileManager()
                .getProfile(event.getPlayer());
        
        String label = event.getMessage().substring(1, event.getMessage().indexOf(' '));
        String[] args = event.getMessage().substring(label.length() + 1).split(" ");
        boolean async = event.isAsynchronous();
        
        if (executeAsyncCommand(sender, label, args, async)) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(executeSyncCommand(sender, label, args, async));
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void onConsoleCommand(@NotNull ServerCommandEvent event) {
        String label = event.getCommand().substring(event.getCommand().indexOf(' '));
        String[] args = event.getCommand().substring(label.length() + 1).split(" ");
        boolean async = event.isAsynchronous();
        
        if (executeAsyncCommand(MainPlugin.getInstance().getProfileManager().getConsole(),
                label, args, async)) {
            
            event.setCancelled(true);
            return;
        }
        event.setCancelled(
                executeSyncCommand(MainPlugin.getInstance().getProfileManager().getConsole(),
                        label, args, async));
    }
    
    private boolean executeAsyncCommand(@NotNull Profile sender, @NotNull String label,
            @NotNull String[] args, boolean inAsyncThread) {
        
        Command command = asyncCommands.get(label);
        
        if (command == null) {
            return false;
        }
        AsyncCommandEvent event = new AsyncCommandEvent(sender, command, inAsyncThread);
        
        if (event.isCancelled()) {
            return false;
        }
        if (inAsyncThread) {
            event.getCommand().preExecute(event.getSender(), label, args);
        }
        else {
            new BukkitRunnable() {
                
                @Override
                public void run() {
                    event.getCommand().preExecute(event.getSender(), label, args);
                }
                
            }.runTaskAsynchronously(MainPlugin.getInstance());
        }
        return true;
    }
    
    private boolean executeSyncCommand(@NotNull Profile sender, @NotNull String label,
            @NotNull String[] args, boolean inAsyncThread) {
        
        Command command = syncCommands.get(label);
        
        if (command == null) {
            return false;
        }
        CodeCommandEvent event = new CodeCommandEvent(sender, command, inAsyncThread);
        
        if (event.isCancelled()) {
            return false;
        }
        if (inAsyncThread) {
            new BukkitRunnable() {
                
                @Override
                public void run() {
                    event.getCommand().preExecute(event.getSender(), label, args);
                }
                
            }.runTask(MainPlugin.getInstance());
        }
        else {
            event.getCommand().preExecute(event.getSender(), label, args);
        }
        return true;
    }
}