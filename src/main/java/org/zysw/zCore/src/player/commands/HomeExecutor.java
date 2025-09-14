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
import org.zysw.zCore.src.utils.GetPlayerData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeExecutor implements CommandExecutor, TabExecutor {
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
            Map<String, Component> placeholders = Map.of();
            player.sendMessage(ConvertStringToComponent.convertWithComponents(ZCore.getInstance().getConfig().getString("home-settings.home.message"), placeholders));
            player.teleport(homeLocation);
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
