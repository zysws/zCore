package org.zysw.zCore.src.utils;

import org.bukkit.Location;
import org.zysw.zCore.src.core.YAMLFileManager;

import java.io.File;

public class GetSettings {

    private static final GetSettings INSTANCE = new GetSettings();

    public Object getData(File yml, String path) {

        Object obj = YAMLFileManager.getInstance().get(yml, path);

        if (obj instanceof Location) {
            return (Location) obj;
        } else if (obj instanceof String) {
            return (String) obj;
        }

        return obj;
    }


    public static GetSettings getInstance() {
        return INSTANCE;
    }
}
