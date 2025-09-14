package org.zysw.zCore.src.player.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zysw.zCore.src.ZCore;
import org.zysw.zCore.src.utils.ConvertStringToComponent;
import org.zysw.zCore.src.utils.TpaManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TpaAcceptExecutor implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();

        if (!TpaManager.hasRequest(targetUUID)) {
            Map<String, String> placeholders = Map.of();

            target.sendMessage(ConvertStringToComponent.convert(ZCore.getInstance().getConfig().getString("tpa-settings.no-requests-message"), placeholders));
            return true;
        }

        UUID requesterUUID = TpaManager.getRequester(targetUUID);
        Player requester = Bukkit.getPlayer(requesterUUID);

        if (requester == null || !requester.isOnline()) {
            Map<String, String> placeholders = Map.of();

            target.sendMessage(ConvertStringToComponent.convert(ZCore.getInstance().getConfig().getString("tpa-settings.no-longer-online-message"), placeholders));
            TpaManager.removeRequest(targetUUID);
            return true;
        }

        // Teleport the requester to the target
        requester.teleport(target.getLocation());
        Map<String, Component> requesterPlaceholders = Map.of(
                "targetplayer", Component.text(target.getName())
        );

        requester.sendMessage(ConvertStringToComponent.convertWithComponents(ZCore.getInstance().getConfig().getString("tpa-settings.tpaccept.requester-message"), requesterPlaceholders));

        Map<String, Component> targetPlayerPlaceholders = Map.of(
                "requester", Component.text(requester.getName())
        );
        target.sendMessage(ConvertStringToComponent.convertWithComponents(ZCore.getInstance().getConfig().getString("tpa-settings.tpaccept.target-player-message"), targetPlayerPlaceholders));

        // Remove the request from map
        TpaManager.removeRequest(targetUUID);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }

}

