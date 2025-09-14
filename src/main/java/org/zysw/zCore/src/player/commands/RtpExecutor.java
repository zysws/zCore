package org.zysw.zCore.src.player.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.zysw.zCore.src.ZCore;
import org.zysw.zCore.src.utils.ConvertStringToComponent;

import java.util.Random;

public class RtpExecutor implements CommandExecutor {

    private final JavaPlugin plugin;
    private final int radius = 5000;

    public RtpExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        String message = (String) ZCore.getInstance().getConfig().get("rtp-settings.searching-message");
        Component finalMessage = ConvertStringToComponent.convertWithComponents(message, null);
        player.sendMessage(finalMessage);

        new BukkitRunnable() {
            @Override
            public void run() {
                World world = player.getWorld();
                Random random = new Random();

                Location safeLocation = null;

                while (true) {
                    int x = random.nextInt(radius * 2) - radius;
                    int z = random.nextInt(radius * 2) - radius;

                    Location highest = world.getHighestBlockAt(x, z).getLocation().add(0, 1, 0);
                    Material blockBelow = highest.clone().subtract(0, 1, 0).getBlock().getType();

                    if (isSafeBlock(blockBelow)) {
                        safeLocation = highest;
                        break;
                    }
                }

                Location finalSafeLocation = safeLocation;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(finalSafeLocation);
                        String message = (String) ZCore.getInstance().getConfig().get("rtp-settings.found-message");
                        Component finalMessage = ConvertStringToComponent.convertWithComponents(message, null);

                        player.sendMessage(finalMessage);
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);

        return true;
    }

    private boolean isSafeBlock(Material material) {
        return material.isSolid()
                && material != Material.LAVA
                && material != Material.CACTUS
                && material != Material.FIRE
                && material != Material.MAGMA_BLOCK
                && material != Material.POWDER_SNOW
                && !material.name().contains("WATER");
    }
}
