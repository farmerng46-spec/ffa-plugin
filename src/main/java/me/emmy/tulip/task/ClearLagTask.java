package me.emmy.tulip.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project FFA
 * @date 07/09/2024 - 15:10
 * Optimized for high-performance FFA servers.
 */
public class ClearLagTask extends BukkitRunnable {

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            // Ottenere direttamente la collezione di frecce è molto più veloce 
            // che ciclare tutte le entità del mondo.
            world.getEntitiesByClass(Arrow.class).forEach(arrow -> {
                if (arrow.isOnGround() || arrow.isDead()) {
                    arrow.remove();
                }
            });
        }
    }
}