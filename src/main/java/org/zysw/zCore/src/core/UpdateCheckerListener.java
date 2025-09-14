package org.zysw.zCore.src.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCheckerListener implements Listener {

    private final JavaPlugin plugin;
    private final String currentVersion;

    public UpdateCheckerListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion(); // from plugin.yml
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.isOp()) {
            return;
        }

        // Run async so it doesn't block the server
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/zysws/zCore/refs/heads/master/version.txt");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int status = conn.getResponseCode();
                if (status == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String latestVersion = in.readLine().trim();
                    in.close();

                    if (!latestVersion.equals(currentVersion)) {
                        Component downloadMessage = Component.text(" [Download]")
                                .color(NamedTextColor.GOLD)
                                .clickEvent(ClickEvent.openUrl("https://github.com/zysws/zCore/releases"))
                                .hoverEvent(HoverEvent.showText(
                                        Component.text("Click to download the latest version")
                                                .color(NamedTextColor.GRAY)
                                ));

                        Component fullMessage = Component.text("[zCore] ")
                                .color(NamedTextColor.GOLD)
                                .append(Component.text("New version available: ", NamedTextColor.WHITE))
                                .append(Component.text(latestVersion, NamedTextColor.GREEN))
                                .append(Component.text(" (You are currently on ", NamedTextColor.WHITE))
                                .append(Component.text(currentVersion, NamedTextColor.RED))
                                .append(Component.text(")", NamedTextColor.WHITE))
                                .append(downloadMessage); // Append download button to the full message

                        player.sendMessage(fullMessage);

                    }
                } else {
                    plugin.getLogger().warning("Could not check version: HTTP status " + status);
                }

            } catch (Exception e) {
                plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());
            }
        });
    }
}

