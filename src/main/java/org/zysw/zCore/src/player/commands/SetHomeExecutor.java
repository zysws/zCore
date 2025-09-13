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

public class SetHomeExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Location location = player.getLocation();
        UUID uuid = player.getUniqueId();


        GetPlayerData.getInstance().saveData(uuid, "homes.home", location);

        Map<String, String> placeholders = Map.of(
                "cords", location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
        );

        player.sendMessage(ConvertStringToComponent.convert(ZCore.getInstance().getConfig().getString("set-home-settings.message"), placeholders));


        return true;
    }
}
