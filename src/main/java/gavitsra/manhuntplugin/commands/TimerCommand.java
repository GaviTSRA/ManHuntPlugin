package gavitsra.manhuntplugin.commands;

import gavitsra.manhuntplugin.Manhuntplugin;
import gavitsra.manhuntplugin.tasks.TimerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TimerCommand implements CommandExecutor {
    Manhuntplugin plugin;

    public TimerCommand(Manhuntplugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            if (sender instanceof Player player) {
                TextComponent msg = Component.text(ChatColor.RED + "Please provide one of these arguments: start | pause | reset");
                player.sendMessage(msg);
            }
            return false;
        }

        switch (args[0]) {
            case "start" -> {
                if (plugin.timer == null) {
                    plugin.timer = new TimerTask(plugin);
                    plugin.timer.runTaskTimer(plugin, 0, 20);
                    if (sender instanceof Player player) {
                        TextComponent msg = Component.text(ChatColor.GREEN + "The timer has been started!");
                        player.sendMessage(msg);
                    }
                }
                else if (plugin.timerPaused)  {
                    plugin.timer.unpause();
                    if (sender instanceof Player player) {
                        TextComponent msg = Component.text(ChatColor.GREEN + "The timer has been unpaused!");
                        player.sendMessage(msg);
                    }
                }
                else if (plugin.timerRunning) {
                    if (sender instanceof Player player) {
                        TextComponent msg = Component.text(ChatColor.RED + "The timer is already running!");
                        player.sendMessage(msg);
                    }
                    return false;
                }

                plugin.timerRunning = true;
                plugin.timerPaused = false;
                return true;
            }
            case "pause" -> {
                if (!plugin.timerRunning) {
                    if (sender instanceof Player player) {
                        TextComponent msg = Component.text(ChatColor.RED + "The timer is not running!");
                        player.sendMessage(msg);
                    }
                    return false;
                }
                else if (plugin.timerPaused) {
                    if (sender instanceof Player player) {
                        TextComponent msg = Component.text(ChatColor.RED + "The timer is already paused!");
                        player.sendMessage(msg);
                    }
                    return false;
                }

                plugin.timer.pause();

                if (sender instanceof Player player) {
                    TextComponent msg = Component.text(ChatColor.GREEN + "The timer has been paused!");
                    player.sendMessage(msg);
                }

                plugin.timerPaused = true;
                return true;
            }
            case "reset" -> {
                if(plugin.timerRunning) plugin.timer.cancel();
                plugin.timer = null;

                if (sender instanceof Player player) {
                    TextComponent msg = Component.text(ChatColor.GREEN + "The timer has been reset!");
                    player.sendMessage(msg);
                }

                plugin.timerRunning = false;
                plugin.timerPaused = false;
                return true;
            }
            default -> {
                if (sender instanceof Player player) {
                    TextComponent msg = Component.text(ChatColor.RED + "Please provide one of these arguments: start | pause | reset");
                    player.sendMessage(msg);
                }
                return false;
            }
        }
    }
}
