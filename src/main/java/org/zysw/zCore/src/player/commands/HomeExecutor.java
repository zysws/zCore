package org.zysw.zCore.src.player.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zysw.zCore.src.ZCore;
import org.zysw.zCore.src.utils.ConvertStringToComponent;
import org.zysw.zCore.src.utils.GetPlayerData;

import java.util.Map;
import java.util.UUID;

public class HomeExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        Location homeLocation = (Location) GetPlayerData.getInstance().getData(uuid, "homes.home");

        if (homeLocation == null) {
            player.sendMessage("You don't have a home set.");
        } else {
            Map<String, String> placeholders = Map.of();
            player.sendMessage(ConvertStringToComponent.convert(ZCore.getInstance().getConfig().getString("home-settings.home.message"), placeholders));
            player.teleport(homeLocation);
        }


        return true;
    }
}
