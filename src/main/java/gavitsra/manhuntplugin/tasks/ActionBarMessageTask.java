package gavitsra.manhuntplugin.tasks;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarMessageTask extends BukkitRunnable {
    Manhuntplugin plugin;
    Player player;
    public String timer = "";

    public ActionBarMessageTask(Manhuntplugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        TextComponent finalMessage = Component.text("");

        double distance = 0;
        ItemStack tracker = null;
        if (plugin.compassLocatorManager.isValidTracker(player.getInventory().getItemInMainHand())) {
            tracker = player.getInventory().getItemInMainHand();
        } else if (plugin.compassLocatorManager.isValidTracker(player.getInventory().getItemInOffHand())) {
            tracker = player.getInventory().getItemInOffHand();
        }

        if (tracker != null) {
            Player trackedPlayer = plugin.compassLocatorManager.getTrackedPlayer(tracker);
            if (trackedPlayer != null) {
                distance = plugin.compassLocatorManager.getTrackedPlayerDistance(player, tracker);
            }
        }
		
        if (distance != 0.0) {
            String playername = PlainTextComponentSerializer.plainText().serialize(plugin.compassLocatorManager.getTrackedPlayer(tracker).displayName());
            if (distance != -1.0) {
                finalMessage = finalMessage.append(Component.text(ChatColor.YELLOW + playername +" is " + (int)distance + " blocks away"));
            } else {
                distance = plugin.compassLocatorManager.getTrackedPlayerPortalDistance(player, tracker);
                finalMessage = finalMessage.append(Component.text(ChatColor.YELLOW + playername + "'s portal is " + (int)distance + " blocks away"));
            }
        }
	    finalMessage = finalMessage.append(Component.text("   " + timer));

        player.sendActionBar(finalMessage);
    }
}
