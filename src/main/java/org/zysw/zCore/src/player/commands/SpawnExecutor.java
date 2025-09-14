package org.zysw.zCore.src.player.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zysw.zCore.src.ZCore;
import org.zysw.zCore.src.utils.ConvertStringToComponent;

import java.util.Map;

public class SpawnExecutor implements CommandExecutor {
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
            Map<String, String> placeholders = Map.of(
                    "cords", spawnLocation.getBlockX() + ", " + spawnLocation.getBlockY() + ", " + spawnLocation.getBlockZ()
            );

            player.sendMessage(ConvertStringToComponent.convert(ZCore.getInstance().getConfig().getString("spawn-settings.spawn.message"), placeholders));
        }


        return true;
    }
}
