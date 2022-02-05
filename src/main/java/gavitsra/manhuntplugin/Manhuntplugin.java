package gavitsra.manhuntplugin;

import gavitsra.manhuntplugin.commands.CompassCommand;
import gavitsra.manhuntplugin.commands.TimerCommand;
import gavitsra.manhuntplugin.listeners.PlayerDeathListener;
import gavitsra.manhuntplugin.listeners.PlayerInteractListener;
import gavitsra.manhuntplugin.listeners.PlayerJoinListener;
import gavitsra.manhuntplugin.listeners.PlayerTeleportListener;
import gavitsra.manhuntplugin.tasks.ActionBarMessageTask;
import gavitsra.manhuntplugin.tasks.CompassLocatorTask;
import gavitsra.manhuntplugin.tasks.TimerTask;
import gavitsra.manhuntplugin.util.CompassLocatorManager;
import gavitsra.manhuntplugin.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Manhuntplugin extends JavaPlugin {

    public TimerTask timer;
    public CompassLocatorManager compassLocatorManager;
    public HashMap<Player, ActionBarMessageTask> playerMessagesTasks;
    public boolean timerRunning = false;
    public boolean timerPaused = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        playerMessagesTasks = new HashMap<>();
        compassLocatorManager = new CompassLocatorManager(this);

        getCommand("compass").setExecutor(new CompassCommand(this));
        getCommand("timer").setExecutor(new TimerCommand(this));
        getCommand("timer").setTabCompleter(new TimerCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this);

        new CompassLocatorTask(this).runTaskTimer(this, 0, 20);

	int pluginId = 14196;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
