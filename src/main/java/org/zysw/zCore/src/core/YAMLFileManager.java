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

    public void set(File ymlFile, String path, Object value) {
        try {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(ymlFile);
            cfg.set(path, value);
            cfg.save(ymlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Object get(File ymlFile, String path) {
        try {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(ymlFile);
            return cfg.get(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static YAMLFileManager getInstance() {
        return INSTANCE;
    }
}
