package me.emmy.tulip;

import lombok.Getter;
import me.emmy.tulip.api.assemble.Assemble;
import me.emmy.tulip.api.assemble.AssembleStyle;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.database.FlatFileService; // Sostituisce Mongo
import me.emmy.tulip.feature.arena.ArenaRepository;
import me.emmy.tulip.feature.cooldown.CooldownRepository;
import me.emmy.tulip.feature.kit.KitRepository;
import me.emmy.tulip.feature.product.ProductRepository;
import me.emmy.tulip.feature.spawn.SpawnHandler;
import me.emmy.tulip.game.GameRepository;
import me.emmy.tulip.game.spawn.FFASpawnHandler;
import me.emmy.tulip.game.spawn.task.FFASpawnTask;
import me.emmy.tulip.profile.ProfileRepository;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.CommandUtility;
import me.emmy.tulip.util.PluginUtil;
import me.emmy.tulip.util.ServerUtil;
import me.emmy.tulip.visual.scoreboard.ScoreboardVisualizer;
import me.emmy.tulip.visual.tablist.task.TablistUpdateTask;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Tulip extends JavaPlugin {

    @Getter
    private static Tulip instance;

    // Services & Repositories
    private ConfigService configService;
    private SpawnHandler spawnHandler;
    private ArenaRepository arenaRepository;
    private KitRepository kitRepository;
    private FlatFileService databaseService; // Cambiato da MongoService
    private ProfileRepository profileRepository;
    private CooldownRepository cooldownRepository;
    private GameRepository gameRepository;
    private FFASpawnHandler ffaSpawnHandler;
    private ProductRepository productRepository;

    @Override
    public void onEnable() {
        instance = this;

        // 1. Inizializzazione Config (Sempre per prima)
        this.configService = new ConfigService();

        // 2. Inizializzazione Database (FlatFile/YAML invece di Mongo)
        this.databaseService = new FlatFileService(this);
        this.databaseService.setup();

        // 3. Caricamento Repositories
        this.loadRepositories();

        // 4. Registrazione Comandi e Listeners
        CommandUtility.registerCommands();
        
        // Questo metodo deve essere cross-version friendly!
        PluginUtil.registerListenersInPackage(this, "me.emmy.tulip");

        // 5. Setup Mondo e Task
        ServerUtil.setupWorld();
        this.runTasks();
        this.loadScoreboard();

        CC.sendStartupMessage();
    }

    private void loadRepositories() {
        this.spawnHandler = new SpawnHandler();
        this.spawnHandler.loadSpawn();

        this.arenaRepository = new ArenaRepository();
        this.arenaRepository.loadArenas();

        this.kitRepository = new KitRepository();
        this.kitRepository.loadKits();

        this.gameRepository = new GameRepository();
        this.gameRepository.loadFFAMatches();

        this.ffaSpawnHandler = new FFASpawnHandler();
        this.ffaSpawnHandler.loadFFASpawn();

        this.profileRepository = new ProfileRepository();
        this.profileRepository.initializeEveryProfile();

        this.cooldownRepository = new CooldownRepository();

        this.productRepository = new ProductRepository();
        this.productRepository.loadProducts();
    }

    @Override
    public void onDisable() {
        // Salvataggio profili prima dello spegnimento
        if (this.profileRepository != null) {
            this.profileRepository.getProfiles().forEach((uuid, profile) -> profile.saveProfile());
        }

        if (this.arenaRepository != null) this.arenaRepository.saveArenas();
        if (this.kitRepository != null) this.kitRepository.saveKits();
        if (this.gameRepository != null) this.gameRepository.saveFFAMatches();

        ServerUtil.disconnectPlayers();
        ServerUtil.stopTasks();

        CC.sendShutdownMessage();
    }

    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        // MODERN supporta i colori HEX (1.16+), ma Assemble gestisce il fallback per la 1.8
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    private void runTasks() {
        if (this.ffaSpawnHandler.getCuboid() != null) {
            new FFASpawnTask(this.ffaSpawnHandler.getCuboid(), this).runTaskTimer(this, 0, 20);
        }
        
        if (configService.getTablistConfig().getBoolean("tablist.enabled")) {
            new TablistUpdateTask().runTaskTimer(this, 0L, 20L);
        }
    }
}