package com.flappyburdddd.mcautomacebreach.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("mcautomacebreach");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("mcautomacebreach");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("config.json");
    
    public static ConfigData configData = new ConfigData();
    
    public static class ConfigData {
        public boolean autoBreachSwapEnabled = false;
        public boolean autoStunSlamEnabled = false;
        public boolean shieldDrainEnabled = false;
        public int shieldDrainDelayTicks = 3; // Delay between shield drain hits
    }
    
    public static void loadConfig() {
        try {
            Files.createDirectories(CONFIG_DIR);
            
            if (Files.exists(CONFIG_FILE)) {
                String json = new String(Files.readAllBytes(CONFIG_FILE), StandardCharsets.UTF_8);
                configData = GSON.fromJson(json, ConfigData.class);
                LOGGER.info("[McAutomaceBreach] Config loaded from " + CONFIG_FILE);
            } else {
                saveConfig();
                LOGGER.info("[McAutomaceBreach] Config file created at " + CONFIG_FILE);
            }
        } catch (IOException e) {
            LOGGER.error("[McAutomaceBreach] Failed to load config", e);
        }
    }
    
    public static void saveConfig() {
        try {
            Files.createDirectories(CONFIG_DIR);
            String json = GSON.toJson(configData);
            Files.write(CONFIG_FILE, json.getBytes(StandardCharsets.UTF_8));
            LOGGER.info("[McAutomaceBreach] Config saved to " + CONFIG_FILE);
        } catch (IOException e) {
            LOGGER.error("[McAutomaceBreach] Failed to save config", e);
        }
    }
}
