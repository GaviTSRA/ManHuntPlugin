package gavitsra.manhuntplugin.tasks;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Objects;

public class CompassLocatorTask extends BukkitRunnable {
    Manhuntplugin plugin;

    public CompassLocatorTask(Manhuntplugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            for (int x = 0; x < player.getInventory().getSize() - 1; x++) {
                ItemStack item = player.getInventory().getItem(x);

                if (item == null) continue;
                if (item.getType() != Material.COMPASS) continue;
                if (!item.getItemMeta().hasDisplayName()) continue;
                if (!PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName()).contains("Player Locator - ")) continue;

                String playerName = PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName()).split(" - ")[1];
                Player searchedPlayer = null;

                System.out.println(playerName);

                for (Player player2 : players) {
                    if (Objects.equals(player2.getName(), playerName)) {
                        searchedPlayer = player2;
                    }
                }

                if (searchedPlayer == null) {
                    player.setCompassTarget(new Location(player.getWorld(), 0, 0, 0));
                    final TextComponent msg = Component.text(ChatColor.RED+"One tracker could not find it's player");
                    player.clearTitle();
                    player.sendActionBar(msg);
                } else {
                    player.setCompassTarget(searchedPlayer.getLocation());
                }
            }
        }
    }
}
