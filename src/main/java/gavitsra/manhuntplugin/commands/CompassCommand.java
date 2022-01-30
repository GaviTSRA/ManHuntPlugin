package gavitsra.manhuntplugin.commands;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CompassCommand implements CommandExecutor {
    Manhuntplugin plugin;
    NamespacedKey trackingPlayersKey;
    NamespacedKey selectedPlayerKey;

    public CompassCommand(Manhuntplugin plugin) {
        this.trackingPlayersKey = new NamespacedKey(plugin, "trackingplayers");
        this.selectedPlayerKey = new NamespacedKey(plugin, "selectedplayer");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && args.length > 0) {
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta meta = compass.getItemMeta();

            StringBuilder playersBuilder = new StringBuilder();

            for (String arg : args) {
                playersBuilder.append(arg).append(",");
            }

            String players = playersBuilder.substring(0, playersBuilder.length() - 1);

            TextComponent name = Component.text("Player Locator");
            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text("Tracking players: " + players));

            meta.displayName(name);
            meta.lore(lore);

            meta.getPersistentDataContainer().set(trackingPlayersKey, PersistentDataType.STRING, players);
            meta.getPersistentDataContainer().set(selectedPlayerKey, PersistentDataType.INTEGER, 0);
            compass.setItemMeta(meta);

            player.getInventory().addItem(compass);

            return true;
        }

        return false;
    }
}
