package gavitsra.manhuntplugin.tasks;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

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

                if (item == null) continue;
                if (item.getType() != Material.COMPASS) continue;

                PersistentDataContainer persistentData = item.getItemMeta().getPersistentDataContainer();

                if (!persistentData.has(trackingPlayersKey, PersistentDataType.STRING)) continue;
                if (!persistentData.has(selectedPlayerKey, PersistentDataType.INTEGER)) continue;

                String trackedPlayersData = persistentData.get(trackingPlayersKey, PersistentDataType.STRING);
                ArrayList<String> trackedPlayers = new ArrayList<>();
                if (trackedPlayersData.contains(",")) {
                    Collections.addAll(trackedPlayers, trackedPlayersData.split(","));
                }
                else trackedPlayers.add(trackedPlayersData);

                int trackedPlayerIndex = persistentData.get(selectedPlayerKey, PersistentDataType.INTEGER);
                String playerName = trackedPlayers.get(trackedPlayerIndex);

                Player searchedPlayer = null;
                for (Player player2 : players) {
                    if (Objects.equals(player2.getName(), playerName)) {
                        searchedPlayer = player2;
                    }
                }

                if (searchedPlayer == null) {
                    player.setCompassTarget(new Location(player.getWorld(), 0, 0, 0));
                    final TextComponent msg = Component.text(ChatColor.RED+"At least one tracker could not find it's player");
                    player.clearTitle();
                    player.sendActionBar(msg);
                } else {
                    player.setCompassTarget(searchedPlayer.getLocation());
                }
            }
        }
    }
}
