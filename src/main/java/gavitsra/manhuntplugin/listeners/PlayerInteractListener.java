package gavitsra.manhuntplugin.listeners;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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

            // Check if the item is a valid tracker
            if(!plugin.compassLocatorManager.isValidTracker(selectedItem)) return;

            // Get the tracked players and the tracked player index
            ArrayList<String> trackedPlayers = plugin.compassLocatorManager.getTrackedPlayers(selectedItem);
            int trackedPlayerIndex = plugin.compassLocatorManager.getTrackedPlayerIndex(selectedItem);

            // Update the tracked player index
            if (trackedPlayers.size() - 1 <= trackedPlayerIndex) {
                trackedPlayerIndex = 0;
                plugin.compassLocatorManager.setTrackedPlayerIndex(selectedItem, 0);
            } else {
		        trackedPlayerIndex++;
                plugin.compassLocatorManager.setTrackedPlayerIndex(selectedItem, trackedPlayerIndex);
            }

            // Send the player a message
            TextComponent msg = Component.text("Now tracking player: " + trackedPlayers.get(trackedPlayerIndex));
            player.sendMessage(msg);
        }
    }

}
