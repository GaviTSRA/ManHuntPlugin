package gavitsra.manhuntplugin.listeners;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerInteractListener implements Listener {
    Manhuntplugin plugin;

    public PlayerInteractListener(Manhuntplugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            Player player = event.getPlayer();

            ItemStack selectedItem = player.getInventory().getItemInMainHand();
            if (selectedItem.getType() != Material.COMPASS) return;

            NamespacedKey trackingPlayersKey = new NamespacedKey(plugin, "trackingplayers");
            NamespacedKey selectedPlayerKey = new NamespacedKey(plugin, "selectedplayer");
	    ItemMeta meta = selectedItem.getItemMeta();
            PersistentDataContainer persistentData = meta.getPersistentDataContainer();

            if (!persistentData.has(trackingPlayersKey, PersistentDataType.STRING)) return;
            if (!persistentData.has(selectedPlayerKey, PersistentDataType.INTEGER)) return;

            String trackedPlayersData = persistentData.get(trackingPlayersKey, PersistentDataType.STRING);
            ArrayList<String> trackedPlayers = new ArrayList<>();
            if (trackedPlayersData.contains(",")) {
                Collections.addAll(trackedPlayers, trackedPlayersData.split(","));
            }
            else trackedPlayers.add(trackedPlayersData);

            int trackedPlayerIndex = persistentData.get(selectedPlayerKey, PersistentDataType.INTEGER);

            TextComponent msg;
            if (trackedPlayers.size() - 1 <= trackedPlayerIndex) {
		trackedPlayerIndex = 0;
 		persistentData.set(selectedPlayerKey, PersistentDataType.INTEGER, trackedPlayerIndex);
                msg = Component.text("Now tracking player: " + trackedPlayers.get(trackedPlayerIndex));
            } else {
		trackedPlayerIndex++;
                persistentData.set(selectedPlayerKey, PersistentDataType.INTEGER, trackedPlayerIndex);
                msg = Component.text("Now tracking player: " + trackedPlayers.get(trackedPlayerIndex));
            }
	    selectedItem.setItemMeta(meta);
	    
            player.sendMessage(msg);
        }
    }

}
