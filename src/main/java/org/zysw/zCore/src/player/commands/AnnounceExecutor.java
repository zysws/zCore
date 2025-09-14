package org.zysw.zCore.src.player.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zysw.zCore.src.ZCore;
import org.zysw.zCore.src.utils.ConvertStringToComponent;

import java.util.List;

public class AnnounceExecutor implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage("usage: /announce <message>");
        }

        Component prefix = ConvertStringToComponent.convertWithComponents((String) ZCore.getInstance().getConfig().getString("global.announcement-prefix"), null);

        String joinedArgs = String.join(" ", args);
        Component message = ConvertStringToComponent.convertWithComponents(joinedArgs, null);

        Component finalMessage = prefix.append(Component.text(" ")).append(message);

        Bukkit.broadcast(finalMessage);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
