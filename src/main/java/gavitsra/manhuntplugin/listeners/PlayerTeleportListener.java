package gavitsra.manhuntplugin.listeners;

import gavitsra.manhuntplugin.Manhuntplugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {
    Manhuntplugin plugin;

    public PlayerTeleportListener(Manhuntplugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            plugin.compassLocatorManager.setPlayerPortalLocation(player.getWorld().getName(), player, event.getFrom());
        }
    }
}
