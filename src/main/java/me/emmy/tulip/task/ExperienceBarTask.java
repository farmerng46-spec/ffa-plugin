package me.emmy.tulip.task;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.cooldown.Cooldown;
import me.emmy.tulip.feature.cooldown.CooldownRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project FFA
 * @date 08/09/2024 - 23:19
 * Optimized for cross-version performance.
 */
public class ExperienceBarTask extends BukkitRunnable {

    @Override
    public void run() {
        CooldownRepository repository = Tulip.getInstance().getCooldownRepository();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Cooldown cooldown = repository.getCooldown(player.getUniqueId(), "ENDERPEARL");

            if (cooldown != null && cooldown.isActive()) {
                double remaining = cooldown.remainingTime() / 1000.0; // Convertiamo ms in secondi
                
                // Imposta il numero sopra la barra (es. 15, 14, 13...)
                player.setLevel((int) Math.ceil(remaining));

                // Calcola la percentuale della barra verde (da 1.0 a 0.0)
                // Usiamo il tempo totale iniziale del cooldown per una barra precisa
                float progress = (float) (cooldown.remainingTime() / (double) cooldown.getDuration());
                player.setExp(Math.max(0.0f, Math.min(1.0f, progress)));
                
            } else {
                // Reset se il cooldown è finito o non esiste
                if (player.getLevel() > 0 || player.getExp() > 0) {
                    player.setLevel(0);
                    player.setExp(0);
                }
            }
        }
        // NOTA: Non chiamare MAI cancel() qui dentro basandoti su un giocatore, 
        // altrimenti fermi il cooldown a tutto il server.
    }
}