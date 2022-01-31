package gavitsra.manhuntplugin.listeners;

import gavitsra.manhuntplugin.Manhuntplugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    Manhuntplugin plugin;

    public PlayerLeaveListener(Manhuntplugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.playerMessagesTasks.get(player).cancel();
        plugin.playerMessagesTasks.remove(player);
    }
}
