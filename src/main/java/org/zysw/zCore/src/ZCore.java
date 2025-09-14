package org.zysw.zCore.src;

import org.bukkit.plugin.java.JavaPlugin;
import org.zysw.zCore.src.player.commands.*;

import java.io.File;

public final class ZCore extends JavaPlugin {

    private static ZCore instance;

    @Override
    public void onEnable() {
        // startup
        instance = this;
        saveDefaultConfig();
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs(); // creates the data folder
        }

        File playersFolder = new File(getDataFolder(), "players");
        if (!playersFolder.exists()) {
            playersFolder.mkdirs(); // creates the players folder
        }

        getCommand("sethome").setExecutor(new SetHomeExecutor());
        getCommand("home").setExecutor(new HomeExecutor());
        getCommand("setspawn").setExecutor(new SetSpawnExecutor());
        getCommand("spawn").setExecutor(new SpawnExecutor());
        getCommand("tpa").setExecutor(new TpaExecutor());
        getCommand("tpaccept").setExecutor(new TpaAcceptExecutor());
        getCommand("tpadeny").setExecutor(new TpaDenyExecutor());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static ZCore getInstance() {
        return instance;
    }
}
