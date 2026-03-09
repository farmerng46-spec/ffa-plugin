package me.emmy.tulip.config;

import lombok.Getter;
import me.emmy.tulip.Tulip;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ConfigService {
    @Getter private static ConfigService instance;

    private final Tulip plugin = Tulip.getInstance();

    private final Map<String, File> configFiles = new HashMap<>();
    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();

    // Cache delle configurazioni per accesso istantaneo
    private FileConfiguration settingsConfig, localeConfig, hotbarConfig, 
            scoreboardConfig, tablistConfig, arenasConfig, kitsConfig, 
            ffaConfig, gameMenuConfig, settingsMenuConfig, 
            kitEditorMenuConfig, kitEditorSelectMenuConfig, statsMenuConfig;

    private final String[] configFileNames = {
            "settings.yml", "locale.yml", "hotbar.yml",
            "storage/arenas.yml", "storage/kits.yml", "storage/ffa.yml",
            "visual/scoreboard.yml", "visual/tablist.yml",
            "menus/game-menu.yml", "menus/settings-menu.yml", "menus/kit-editor-menu.yml", 
            "menus/kit-editor-select-menu.yml", "menus/stats-menu.yml"
    };

    public ConfigService() {
        instance = this;
        reloadConfigs();
    }

    /**
     * Carica o ricarica tutti i file nella RAM.
     */
    public void reloadConfigs() {
        for (String fileName : configFileNames) {
            File configFile = new File(plugin.getDataFolder(), fileName);
            
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                plugin.saveResource(fileName, false);
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            
            configFiles.put(fileName, configFile);
            fileConfigurations.put(fileName, config);
        }

        // Assegnazione delle variabili per accesso rapido (evita lookup nella Map)
        this.settingsConfig = fileConfigurations.get("settings.yml");
        this.localeConfig = fileConfigurations.get("locale.yml");
        this.hotbarConfig = fileConfigurations.get("hotbar.yml");
        this.scoreboardConfig = fileConfigurations.get("visual/scoreboard.yml");
        this.tablistConfig = fileConfigurations.get("visual/tablist.yml");
        this.arenasConfig = fileConfigurations.get("storage/arenas.yml");
        this.kitsConfig = fileConfigurations.get("storage/kits.yml");
        this.ffaConfig = fileConfigurations.get("storage/ffa.yml");
        this.gameMenuConfig = fileConfigurations.get("menus/game-menu.yml");
        this.settingsMenuConfig = fileConfigurations.get("menus/settings-menu.yml");
        this.kitEditorMenuConfig = fileConfigurations.get("menus/kit-editor-menu.yml");
        this.kitEditorSelectMenuConfig = fileConfigurations.get("menus/kit-editor-select-menu.yml");
        this.statsMenuConfig = fileConfigurations.get("menus/stats-menu.yml");
    }

    /**
     * Ritorna la configurazione caricata in RAM (Velocissimo).
     */
    public FileConfiguration getConfig(String fileName) {
        return fileConfigurations.get(fileName);
    }

    /**
     * Salva una specifica configurazione su disco.
     */
    public void saveConfig(String fileName) {
        FileConfiguration config = fileConfigurations.get(fileName);
        File file = configFiles.get(fileName);
        
        if (config != null && file != null) {
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save " + fileName + "!");
            }
        }
    }

    /**
     * Salva tutto (da usare in onDisable).
     */
    public void saveAll() {
        configFiles.keySet().forEach(this::saveConfig);
    }
}