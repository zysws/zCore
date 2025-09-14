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

public class SetSpawnExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Location playerLocation = player.getLocation();


        ZCore.getInstance().getConfig().set("global.spawn.location", playerLocation);

        Map<String, String> placeholders = Map.of(
                "cords", playerLocation.getBlockX() + ", " + playerLocation.getBlockY() + ", " + playerLocation.getBlockZ()
        );
        player.sendMessage(ConvertStringToComponent.convert(ZCore.getInstance().getConfig().getString("spawn-settings.set-spawn.message"), placeholders));


        return true;
    }
}
