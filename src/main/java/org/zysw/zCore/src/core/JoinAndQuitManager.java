package org.zysw.zCore.src.core;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.zysw.zCore.src.ZCore;
import org.zysw.zCore.src.utils.ConvertStringToComponent;

import java.util.Map;

public class JoinAndQuitManager implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        Boolean DefaultJoinMessage = (Boolean) ZCore.getInstance().getConfig().get("global.defaultMinecraftJoinMessage");

        if (!DefaultJoinMessage) {
            joinEvent.setJoinMessage(null);
        }

        Player player = joinEvent.getPlayer();

        String joinMessage = (String) ZCore.getInstance().getConfig().get("global.join-message");

        Map<String, Component> placeholders = Map.of(
                "player", Component.text(player.getName())
        );

        Component finalMessage = ConvertStringToComponent.convertWithComponents(joinMessage, placeholders);

        Bukkit.broadcast(finalMessage);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        Boolean DefaultQuitMessage = (Boolean) ZCore.getInstance().getConfig().get("global.defaultMinecraftQuitMessage");

        if (!DefaultQuitMessage) {
            quitEvent.setQuitMessage(null);
        }

        Player player = quitEvent.getPlayer();

        String quitMessage = (String) ZCore.getInstance().getConfig().get("global.quit-message");

        Map<String, Component> placeholders = Map.of(
                "player", Component.text(player.getName())
        );

        Component finalMessage = ConvertStringToComponent.convertWithComponents(quitMessage, placeholders);

        Bukkit.broadcast(finalMessage);
    }
}
