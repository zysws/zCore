package org.zysw.zCore.src;

import org.bukkit.plugin.java.JavaPlugin;
import org.zysw.zCore.src.player.commands.setHomeExecutor;

import java.io.File;

public final class ZCore extends JavaPlugin {

    private static ZCore instance;

    @Override
    public void onEnable() {
        // startup
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs(); // creates the data folder
        }

        File playersFolder = new File(getDataFolder(), "players");
        if (!playersFolder.exists()) {
            playersFolder.mkdirs(); // creates the players folder
        }

        File settingsFolder = new File(getDataFolder(), "settings");
        if (!settingsFolder.exists()) {
            settingsFolder.mkdirs(); // creates the settings folder
        }

        getCommand("sethome").setExecutor(new setHomeExecutor());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ZCore getInstance() {
        return instance;
    }
}
