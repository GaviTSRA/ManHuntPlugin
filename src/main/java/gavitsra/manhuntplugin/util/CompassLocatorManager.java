package gavitsra.manhuntplugin.util;

import gavitsra.manhuntplugin.Manhuntplugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class CompassLocatorManager {
    public NamespacedKey trackingPlayersKey;
    public NamespacedKey selectedPlayerKey;
    Manhuntplugin plugin;

    public CompassLocatorManager(Manhuntplugin plugin) {
        this.trackingPlayersKey = new NamespacedKey(plugin, "trackingPlayers");
        this.selectedPlayerKey = new NamespacedKey(plugin, "selectedPlayer");
        this.plugin = plugin;
    }

    public boolean isValidTracker(@Nullable ItemStack tracker) {
        if (tracker == null) return false;
        if (tracker.getType() != Material.COMPASS) return false;

        PersistentDataContainer persistentData = tracker.getItemMeta().getPersistentDataContainer();
        if (!persistentData.has(this.trackingPlayersKey, PersistentDataType.STRING)) return false;
        return persistentData.has(this.selectedPlayerKey, PersistentDataType.INTEGER);
    }

    @Nullable
    public Player getTrackedPlayer(ItemStack tracker) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        ArrayList<String> trackedPlayers = getTrackedPlayers(tracker);

        String playerName = trackedPlayers.get(getTrackedPlayerIndex(tracker));

        Player trackedPlayer = null;
        for (Player player : players) {
            if (Objects.equals(player.getName(), playerName)) {
                trackedPlayer = player;
            }
        }

        return trackedPlayer;
    }

    public ArrayList<String> getTrackedPlayers(ItemStack tracker) {
        String trackedPlayersData = tracker.getItemMeta().getPersistentDataContainer().get(trackingPlayersKey, PersistentDataType.STRING);
        ArrayList<String> trackedPlayers = new ArrayList<>();
        if (trackedPlayersData.contains(",")) {
            Collections.addAll(trackedPlayers, trackedPlayersData.split(","));
        }
        else trackedPlayers.add(trackedPlayersData);

        return trackedPlayers;
    }

    public ItemStack setTrackedPlayers(ArrayList<String> players, ItemStack tracker) {
        ItemMeta meta = tracker.getItemMeta();

        StringBuilder playersBuilder = new StringBuilder();

        for (String player : players) {
            playersBuilder.append(player).append(",");
        }

        String finalPlayers = playersBuilder.substring(0, playersBuilder.length() - 1);

        meta.getPersistentDataContainer().set(trackingPlayersKey, PersistentDataType.STRING, finalPlayers);
        tracker.setItemMeta(meta);
        return tracker;
    }

    public int getTrackedPlayerIndex(ItemStack tracker) {
        if (tracker.getItemMeta().getPersistentDataContainer().get(selectedPlayerKey, PersistentDataType.INTEGER) == null) throw new IllegalStateException("Not a valid tracker!");
        else return tracker.getItemMeta().getPersistentDataContainer().get(selectedPlayerKey, PersistentDataType.INTEGER);
    }

    public ItemStack setTrackedPlayerIndex(ItemStack tracker, int index) {
        ItemMeta meta = tracker.getItemMeta();
        meta.getPersistentDataContainer().set(selectedPlayerKey, PersistentDataType.INTEGER, index);
        tracker.setItemMeta(meta);
        return tracker;
    }

    public double getTrackedPlayerDistance(Player player, ItemStack tracker) {
        Player trackedPlayer = getTrackedPlayer(tracker);
        try { return player.getLocation().distance(trackedPlayer.getLocation()); }
        catch (IllegalArgumentException err) {
            return -1;
        }
    }

    public Location getPlayerPortalLocation(String worldName, Player player, Player trackedPlayer) {
        PersistentDataContainer data = trackedPlayer.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "portal/" + worldName);
        if (data.get(key, PersistentDataType.STRING) == null) {
            return new Location(player.getWorld(), -1d, -1d ,-1d);
        } else {
            String loc = data.get(key, PersistentDataType.STRING);
            double x = Double.parseDouble(loc.split(",")[0]);
            double y = Double.parseDouble(loc.split(",")[1]);
            double z = Double.parseDouble(loc.split(",")[2]);
            return new Location(player.getWorld(), x, y, z);
        }
    }

    public void setPlayerPortalLocation(String worldName, Player player, Location loc) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "portal/" + worldName);
        data.set(key, PersistentDataType.STRING,loc.getX() + "," + loc.getY() + "," + loc.getZ());
    }

    public double getTrackedPlayerPortalDistance(Player player, ItemStack tracker) {
        Player trackedPlayer = getTrackedPlayer(tracker);
        return player.getLocation().distance(getPlayerPortalLocation(player.getWorld().getName(), player, trackedPlayer));
    }
}
