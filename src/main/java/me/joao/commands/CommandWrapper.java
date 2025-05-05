package me.joao.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class CommandWrapper extends org.bukkit.command.Command {
    
    private final Command command;
    
    public CommandWrapper(@NotNull Command command) {
        super(command.getName(), "", "/", command.getAliases());
        
        this.command = command;
    }
    
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label,
            @NotNull String[] args) {
        
        command.preExecute(command.getPlugin().getProfileManager().getProfile(sender), label, args);
        return false;
    }
    
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias,
            @NotNull String[] args) throws IllegalArgumentException {
        
        Collection<String> options = command.preTabComplete(
                command.getPlugin().getProfileManager().getProfile(sender), alias, args);
        
        return options == null ? List.of() : List.copyOf(options);
    }
}