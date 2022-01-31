package gavitsra.manhuntplugin.listeners;

import gavitsra.manhuntplugin.Manhuntplugin;
import gavitsra.manhuntplugin.tasks.ActionBarMessageTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    Manhuntplugin plugin;

    public PlayerJoinListener(Manhuntplugin plugin) {
        this.plugin = plugin
;    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.playerMessagesTasks.put(player, new ActionBarMessageTask(plugin, player));
        plugin.playerMessagesTasks.get(player).runTaskTimer(plugin, 0, 1);
    }
}
