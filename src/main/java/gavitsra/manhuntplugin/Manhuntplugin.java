package gavitsra.manhuntplugin;

import gavitsra.manhuntplugin.commands.CompassCommand;

import gavitsra.manhuntplugin.commands.TimerCommand;
import gavitsra.manhuntplugin.listeners.PlayerInteractListener;
import gavitsra.manhuntplugin.tasks.CompassLocatorTask;
import gavitsra.manhuntplugin.tasks.TimerTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class Manhuntplugin extends JavaPlugin {

    public TimerTask timer;
    public boolean timerRunning = false;
    public boolean timerPaused = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("compass").setExecutor(new CompassCommand(this));
        getCommand("timer").setExecutor(new TimerCommand(this));
        getCommand("timer").setTabCompleter(new TimerCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        new CompassLocatorTask(this).runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
