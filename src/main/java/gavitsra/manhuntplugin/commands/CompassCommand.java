package gavitsra.manhuntplugin.commands;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class CompassCommand implements CommandExecutor {
    Manhuntplugin plugin;

    public CompassCommand(Manhuntplugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && args.length > 0) {
            // Creating the item
            ItemStack compass = new ItemStack(Material.COMPASS);

            // Setting the tracked players from the args
            ArrayList<String> players = new ArrayList<>(Arrays.asList(args));
            compass = plugin.compassLocatorManager.setTrackedPlayers(players, compass);

            // Getting the items meta
            ItemMeta meta = compass.getItemMeta();

            // Initializing the items name and lore
            TextComponent name = Component.text("Player Locator");
            ArrayList<Component> lore = new ArrayList<>();

            // Building the items lore
            StringBuilder stringPlayers = new StringBuilder();
            for (String stringPlayer : players) {
                stringPlayers.append(stringPlayer);
            }
            lore.add(Component.text("Tracking players: " + stringPlayers));

            // Setting the items name and lore
            meta.displayName(name);
            meta.lore(lore);

            // Setting the items meta
            compass.setItemMeta(meta);

            // Initializing the item tracked player index
            compass = plugin.compassLocatorManager.setTrackedPlayerIndex(compass,0);

            // Giving the player the item
            player.getInventory().addItem(compass);

            return true;
        }

        return false;
    }
}
