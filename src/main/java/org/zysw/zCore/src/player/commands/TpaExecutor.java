package org.zysw.zCore.src.player.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
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
import org.zysw.zCore.src.utils.CooldownManager;
import org.zysw.zCore.src.utils.TpaManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TpaExecutor implements TabExecutor, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player requester)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("You need to provide a player!");
            return true;
        }

        String cooldownKey = "tpa_" + requester.getUniqueId();
        if (CooldownManager.getInstance().isOnCooldown(cooldownKey)) {
            String msg = (String) ZCore.getInstance().getConfig().get("global.cooldown-message");
            Map<String, Component> cooldownPlaceholders = Map.of(
                    "time-left", Component.text(CooldownManager.getInstance().getCooldownTimeLeft(cooldownKey))
            );
            requester.sendMessage(ConvertStringToComponent.convertWithComponents(msg, cooldownPlaceholders));
            return true; // don't continue if on cooldown
        }


        String playerName = args[0];

        Player targetPlayer = Bukkit.getPlayerExact(playerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            requester.sendMessage("Player not found or not online.");
            return true;
        }

        TpaManager.addRequest(targetPlayer.getUniqueId(), requester.getUniqueId());

        Map<String, Component> targetPlayerPlaceholders = Map.of(
                "accept", Component.text("[Accept]")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.runCommand("/tpaccept"))
                        .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
                                Component.text("Click here to accept tpa request").color(NamedTextColor.DARK_GREEN))),
                "decline", Component.text("[Decline]")
                        .color(NamedTextColor.RED)
                        .clickEvent(ClickEvent.runCommand("/tpadeny"))
                        .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
                                Component.text("Click here to decline tpa request").color(NamedTextColor.DARK_RED))),
                "requester", Component.text(requester.getName())
        );

        Map<String, Component> requesterPlaceholders = Map.of(
                "targetplayer", Component.text(targetPlayer.getName())
        );

        requester.sendMessage(ConvertStringToComponent.convertWithComponents(ZCore.getInstance().getConfig().getString("tpa-settings.tpa.requester-message"), requesterPlaceholders));
        targetPlayer.sendMessage(ConvertStringToComponent.convertWithComponents(ZCore.getInstance().getConfig().getString("tpa-settings.tpa.target-player-message"), targetPlayerPlaceholders));

        CooldownManager.getInstance().setOnCooldown(cooldownKey, 5);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }

}
