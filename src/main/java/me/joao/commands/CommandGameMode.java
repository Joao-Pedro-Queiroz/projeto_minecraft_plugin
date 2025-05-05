package me.joao.commands;

import net.kyori.adventure.text.Component;
import me.joao.profiles.Profile;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class CommandGameMode extends Command {
    
    private final List<String> gamemodeNames = List.of("0", "1", "2", "3", "survival", "creative",
            "adventure", "spectator");
    
    public CommandGameMode() {
        super("gamemode", false, "gm", "gm0", "gm1", "gm2", "gm3", "gms", "gma", "gmc",
                "gme");
    }
    
    @Override
    public void execute(@NotNull Profile sender, @NotNull String label, @NotNull String[] args) {
        String modeName;
        String targetName;
        
        if (label.startsWith("gm") && label.length() > 2) {
            modeName = label.substring(2);
            targetName = args.length > 0 ? args[0] : null;
        }
        else {
            if (args.length == 0) {
                sender.sendMessage(Component.text("Usage: /gamemode <mode> [player]"));
                return;
            }
            modeName = args[0];
            targetName = args.length > 1 ? args[1] : null;
        }
        GameMode mode = getGameMode(modeName);
        
        if (mode == null) {
            sender.sendMessage(Component.text("Invalid gamemode: " + args[0]));
            return;
        }
        Profile target = sender;
        
        if (sender.hasPermission(getPermissionOthers()) && targetName != null) {
            target = getProfile(targetName);
        }
        setGameMode(sender, target, mode);
    }
    
    @Override
    public @Nullable Collection<String> tabComplete(@NotNull Profile sender, @NotNull String label,
            @NotNull String @NotNull [] args) {
        
        if (args.length == 0) {
            if (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")) {
                return gamemodeNames;
            }
            return plugin.getProfileManager().getOnlinePlayerNames(null);
        }
        if (args.length == 1) {
            String arg = args[0];
            
            if (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")) {
                return gamemodeNames.stream().filter(name -> name.startsWith(arg)).toList();
            }
            return plugin.getProfileManager().getOnlinePlayerNames(arg);
        }
        if (args.length == 2) {
            if (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")) {
                return plugin.getProfileManager().getOnlinePlayerNames(args[1]);
            }
        }
        return null;
    }
    
    private void setGameMode(@NotNull Profile sender, @Nullable Profile target,
            @NotNull GameMode mode) {
        
        if (target == null) {
            sender.sendMessage(Component.text("Player not found"));
            return;
        }
        target.setGameMode(mode);
        
        if (sender == target) {
            sender.sendMessage(Component.text("Your gamemode has been set to " + mode));
            return;
        }
        if (!target.isPlayer()) {
            sender.sendMessage(Component.text("You cannot set the gamemode of a non-player"));
            return;
        }
        sender.sendMessage(
                Component.text("You have set " + target.getName() + "'s gamemode to " + mode));
        
        target.sendMessage(Component.text("Your gamemode has been set to " + mode));
    }
    
    @Nullable
    private GameMode getGameMode(@NotNull String modeName) {
        try {
            int modeId = Integer.parseInt(modeName);
            
            if (modeId >= 0 && modeId <= 3) {
                return GameMode.getByValue(modeId);
            }
        }
        catch (NumberFormatException ignored) {
        }
        try {
            return GameMode.valueOf(modeName.toUpperCase());
        }
        catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}