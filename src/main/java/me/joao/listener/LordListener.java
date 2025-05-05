package me.joao.listener;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import me.joao.MainPlugin;
import me.joao.profiles.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class LordListener implements Listener {

    @NonNull
    @NotNull
    private final MainPlugin pl;

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEntry(@NotNull PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Profile profile = pl.getProfileManager().getProfile(event.getPlayer().getUniqueId());

        if (profile.hasPermission("insper.code.mensagem.entrada")) {
            profile.sendMessage(Component.text("&c&lSeja bem-vindo(a) ao Insper Code!"));
        }
        else {
            profile.sendMessage(
                    Component.text("&c&lQue pena, você não pode ver a mensagem de entrada."));
        }
    }
}