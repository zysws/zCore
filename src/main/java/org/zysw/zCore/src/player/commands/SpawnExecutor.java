package org.zysw.zCore.src.player.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zysw.zCore.src.ZCore;
import org.zysw.zCore.src.utils.ConvertStringToComponent;

import java.util.List;
import java.util.Map;

public class SpawnExecutor implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (ZCore.getInstance().getConfig().get("global.spawn.location") == null) {
            player.sendMessage("Spawn has not been set yet.");
            return true;
        } else {
            Location spawnLocation = (Location) ZCore.getInstance().getConfig().get("global.spawn.location");

            player.teleport(spawnLocation);
            Map<String, Component> placeholders = Map.of(
                    "cords", Component.text(spawnLocation.getBlockX() + ", " + spawnLocation.getBlockY() + ", " + spawnLocation.getBlockZ())
            );

            player.sendMessage(ConvertStringToComponent.convertWithComponents(ZCore.getInstance().getConfig().getString("spawn-settings.spawn.message"), placeholders));
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
