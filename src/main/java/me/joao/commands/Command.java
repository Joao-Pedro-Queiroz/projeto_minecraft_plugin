package me.joao.commands;

import lombok.Getter;
import me.joao.MainPlugin;
import me.joao.profiles.Profile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public abstract class Command {
    
    /**
     * The plugin instance.
     *
     * @since 1.0.0
     */
    
    @NotNull
    protected final MainPlugin plugin = MainPlugin.getInstance();
    
    /**
     * The primary name of the command.
     *
     * @since 1.0.0
     */
    
    @NotNull
    private String name;
    
    /**
     * Aliases of the command.
     *
     * @since 1.0.0
     */
    
    @NotNull
    private List<String> aliases;
    
    /**
     * The permission required to execute the command.
     *
     * @since 1.0.0
     */
    
    @NotNull
    private String permission;
    
    /**
     * If this command should run asynchronously.
     *
     * @since 1.0.0
     */
    
    private boolean async;
    
    /**
     * Constructs a new command.
     *
     * @param name The primary name of the command.
     * @param async If this command should run asynchronously.
     * @param aliases Aliases of the command.
     *
     * @since 1.0.0
     */
    
    protected Command(@NotNull String name, boolean async, String @NotNull ... aliases) {
        this.name = name.replace(' ', '_');
        
        if (aliases.length > 0) {
            this.aliases = validateAliases(Arrays.asList(aliases));
        }
        else {
            this.aliases = List.of();
        }
        this.async = async;
        
        permission = generatePermission();
    }
    
    /**
     * Sets the name of the command.
     *
     * @param name The new name of the command.
     *
     * @return The updated command for chaining.
     *
     * @since 1.0.0
     */
    
    public Command setName(@NotNull String name) {
        if (name.isBlank() || name.equalsIgnoreCase(this.name)) {
            return this;
        }
        boolean registered = isRegistered();
        
        if (registered) {
            plugin.getCommandManager().unregisterCommand(this.name);
        }
        boolean customPermission = generatePermission().equals(permission);
        
        this.name = name.replace(' ', '_');
        
        if (!customPermission) {
            permission = generatePermission();
        }
        if (registered) {
            plugin.getCommandManager().registerCommand(this.name, this);
        }
        return this;
    }
    
    private @NotNull @Unmodifiable List<String> validateAliases(@NotNull List<String> aliases) {
        List<String> validated = new ArrayList<>();
        
        for (String alias : aliases) {
            if (alias.isBlank()) {
                continue;
            }
            validated.add(alias.replace(' ', '_'));
        }
        return List.copyOf(validated);
    }
    
    public @NotNull Command setAliases(@NotNull List<String> aliases) {
        boolean registered = isRegistered();
        
        if (registered) {
            this.aliases.forEach(s -> plugin.getCommandManager().unregisterCommandAlias(s, this));
        }
        this.aliases = validateAliases(aliases);
        
        if (registered) {
            register();
        }
        return this;
    }
    
    /**
     * @return Get all names of the command, including the primary name and aliases.
     *
     * @since 1.0.0
     */
    
    public @NotNull List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        
        names.add(name);
        names.addAll(aliases);
        
        return names;
    }
    
    public Command setAsync(boolean async) {
        if (this.async == async) {
            return this;
        }
        boolean registered = isRegistered();
        
        if (registered) {
            unregister();
        }
        this.async = async;
        
        if (registered) {
            register();
        }
        return this;
    }
    
    private @NotNull String generatePermission() {
        return "inspercode.command." + getName().toLowerCase();
    }
    
    public @NotNull Command setPermission(@Nullable String permission) {
        if (permission == null || permission.isBlank()) {
            this.permission = generatePermission();
            return this;
        }
        this.permission = permission.toLowerCase().replace(' ', '_');
        
        return this;
    }
    
    @NotNull
    public String getPermissionOthers() {
        return permission + ".others";
    }
    
    /**
     * Registers this command.
     *
     * @see CommandManager#registerCommand(Command)
     * @since 1.0.0
     */
    
    public void register() {
        plugin.getCommandManager().registerCommand(this);
    }
    
    /**
     * @return {@code true} if the command is registered, {@code false} otherwise.
     *
     * @since 1.0.0
     */
    
    public boolean isRegistered() {
        return plugin.getCommandManager().isRegistered(this);
    }
    
    /**
     * Unregisters this command.
     *
     * @see CommandManager#unregisterCommand(Command)
     * @since 1.0.0
     */
    
    public void unregister() {
        plugin.getCommandManager().unregisterCommand(this);
    }
    
    protected Profile getProfile(@NotNull String name) {
        return plugin.getProfileManager().getProfile(name);
    }
    
    public final void preExecute(@NotNull Profile sender, @NotNull String label,
            @NotNull String[] args) {
        
        if (!sender.hasPermission(getPermission())) {
            //TODO: Send no permission message to sender
            return;
        }
        execute(sender, label, args);
    }
    
    public abstract void execute(@NotNull Profile sender, @NotNull String label,
            @NotNull String[] args);
    
    @Nullable
    public final Collection<String> preTabComplete(@NotNull Profile sender, @NotNull String label,
            @NotNull String[] args) {
        
        if (!sender.hasPermission(getPermission())) {
            return null;
        }
        return tabComplete(sender, label, args);
    }
    
    @Nullable
    public abstract Collection<String> tabComplete(@NotNull Profile sender, @NotNull String label,
            @NotNull String[] args);
}