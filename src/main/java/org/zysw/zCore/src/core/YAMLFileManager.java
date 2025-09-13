package org.zysw.zCore.src.core;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YAMLFileManager {

    private static final YAMLFileManager INSTANCE = new YAMLFileManager();

    private File file;
    private YamlConfiguration config;

    private YAMLFileManager() {
        // private constructor for singleton
    }

    public void load(File ymlFile) {
        this.file = ymlFile;
        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public Object get(String path) {
        return config.get(path);
    }

    public static YAMLFileManager getInstance() {
        return INSTANCE;
    }
}
