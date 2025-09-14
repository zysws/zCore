package org.zysw.zCore.src.utils;

import org.bukkit.Location;
import org.zysw.zCore.src.ZCore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GetPlayerData {

    private static final GetPlayerData INSTANCE = new GetPlayerData();

    public void saveData(UUID uuid, String path, Object value) {
        File playerData = new File(ZCore.getInstance().getDataFolder(), "players/" + uuid.toString() + ".yml");
        if (!playerData.exists()) {
            try {
                playerData.createNewFile();          // create file if needed
            } catch (IOException e) {
                throw new RuntimeException("Failed to create player data file", e);
            }
        }
        YAMLFileManager.getInstance().set(playerData, path, value);
    }


    public Object getData(UUID uuid, String path) {
        File playerData = new File(ZCore.getInstance().getDataFolder(), "players/" + uuid.toString() + ".yml");

        if (!playerData.exists()) return null;

        Object obj = YAMLFileManager.getInstance().get(playerData, path);

        if (obj instanceof Location) {
            return (Location) obj;
        } else if (obj instanceof String) {
            return (String) obj;
        }

        return obj;
    }


    public static GetPlayerData getInstance() {
        return INSTANCE;
    }
}
