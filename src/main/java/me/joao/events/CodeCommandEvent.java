package me.joao.events;

import lombok.*;
import me.joao.commands.Command;
import me.joao.profiles.Profile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class CodeCommandEvent extends Event implements Cancellable {
    
    @Getter
    private static final HandlerList HANDLERS = new HandlerList();
    
    @NonNull
    private Profile sender;
    
    @NonNull
    private Command command;
    
    private boolean cancelled;
    
    public CodeCommandEvent(@NotNull Profile sender, @NotNull Command command, boolean async) {
        super(async);
        
        this.sender = sender;
        this.command = command;
        this.cancelled = false;
    }
    
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}