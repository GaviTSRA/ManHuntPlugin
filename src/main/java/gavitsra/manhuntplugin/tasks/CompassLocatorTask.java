package gavitsra.manhuntplugin.tasks;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.ItemFlag;

import java.util.Collection;

public class CompassLocatorTask extends BukkitRunnable {
    Manhuntplugin plugin;
    NamespacedKey trackingPlayersKey;
    NamespacedKey selectedPlayerKey;

    public CompassLocatorTask(Manhuntplugin plugin) {
        this.plugin = plugin;
        this.trackingPlayersKey = new NamespacedKey(plugin, "trackingPlayers");
        this.selectedPlayerKey = new NamespacedKey(plugin, "selectedPlayer");
    }

    @Override
    public void run() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            for (int x = 0; x < player.getInventory().getSize() - 1; x++) {
                ItemStack item = player.getInventory().getItem(x);

                if (!plugin.compassLocatorManager.isValidTracker(item)) continue;

                Player searchedPlayer = plugin.compassLocatorManager.getTrackedPlayer(item);

		CompassMeta compassMeta = (CompassMeta) item.getItemMeta();
		compassMeta.setLodestoneTracked(false);

                if (searchedPlayer == null) {
                    player.setCompassTarget(new Location(player.getWorld
(), 0, 0, 0));
		    compassMeta.setLodestone(new Location(player.getWorld(), 0, 0, 0));
                } else {
		    if(plugin.compassLocatorManager.getTrackedPlayerDistance(player, item) != -1.0) {
                    	player.setCompassTarget(searchedPlayer.getLocation());
			compassMeta.setLodestone(searchedPlayer.getLocation());
		    } else {
			player.setCompassTarget(plugin.compassLocatorManager.getPlayerPortalLocation(player.getWorld().getName(), player, searchedPlayer));
			compassMeta.setLodestone(plugin.compassLocatorManager.getPlayerPortalLocation(player.getWorld().getName(), player, searchedPlayer));
		    }
		    if(player.getWorld().getName() != "world") { item.setItemMeta(compassMeta); }
                }
            }
        }
    }
}
