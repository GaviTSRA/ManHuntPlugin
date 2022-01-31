package gavitsra.manhuntplugin.tasks;

import gavitsra.manhuntplugin.Manhuntplugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class TimerTask extends BukkitRunnable {
    Manhuntplugin plugin;
    int seconds;
    boolean run;

    public TimerTask(Manhuntplugin plugin) {
        this.plugin = plugin;
        this.seconds = 0;
        this.run = true;
    }

    @Override
    public void run() {
        if(this.run) this.seconds++;
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        String secs = this.seconds % 60 + "";
        String minutes = this.seconds / 60 % 60 + "";
        String hours = this.seconds / 60 / 60 + "";

        if (Integer.parseInt(secs) / 10 < 1) secs = "0" + secs;
        if (Integer.parseInt(minutes) / 10 < 1) minutes = "0" + minutes;
        if (Integer.parseInt(hours) / 10 < 1) hours = "0" + hours;

        final TextComponent msg = Component.text(ChatColor.GREEN + "" + hours + ":" + minutes + ":" + secs);

        //for (Player player : players) {
          //  player.sendActionBar(msg);
//        }
	for (var entry : plugin.playerMessagesTasks.entrySet()) {
	    entry.getValue().timer = ChatColor.GREEN + "" + hours + ":" + minutes + ":" + secs;
	}
    }

    public void pause() {
        this.run = false;
    }

    public void unpause() {
        this.run = true;
    }
}
