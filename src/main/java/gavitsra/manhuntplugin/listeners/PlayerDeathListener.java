package gavitsra.manhuntplugin.listeners;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        for (int i = 0; i < 100; i++) {
	    player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 20, 0, i, 0, 0.1);
        }
    }
}
