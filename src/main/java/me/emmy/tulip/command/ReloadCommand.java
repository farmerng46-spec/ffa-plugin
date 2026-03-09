package me.emmy.tulip.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project FFA
 * @date 28/07/2024 - 23:57
 */
public class ReloadCommand extends BaseCommand {

    @Override
    @Command(name = "tulip.reload", permission = "tulip.admin", aliases = {"trl"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        Tulip plugin = Tulip.getInstance();

        sender.sendMessage(CC.translate("&c&l[Tulip] &7Inizio ricarica dei moduli..."));

        try {
            // 1. Ricarica i file YAML nella RAM
            plugin.getConfigService().reloadConfigs();

            // 2. Ricarica i Repository (Fondamentale per aggiornare kit/arene senza riavviare)
            // Assicurati che questi metodi chiamino internamente i nuovi file ricaricati
            plugin.getArenaRepository().loadArenas();
            plugin.getKitRepository().loadKits();
            plugin.getFfaSpawnHandler().loadFFASpawn();
            
            // 3. Notifica il successo
            sender.sendMessage(CC.translate("&a&l[Tulip] &7Tutti i moduli e le configurazioni sono stati ricaricati con successo!"));
            
        } catch (Exception e) {
            sender.sendMessage(CC.translate("&4&l[!] &cErrore critico durante il reload. Controlla la console."));
            e.printStackTrace();
        }
    }
}