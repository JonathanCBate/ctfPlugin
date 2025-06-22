package com.jonathancbate.mc.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StartCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    // Constructor to get plugin instance for scheduling
    public StartCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int countdown = 5;

        new BukkitRunnable() {
            int secondsLeft = countdown;

            @Override
            public void run() {
                if (secondsLeft < 0) {
                    // Countdown finished, cancel task
                    cancel();
                    // Optionally broadcast "Go!" or something else
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.GREEN + "Go!", "", 10, 70, 20);
                    }
                    return;
                }

                // Broadcast countdown as a title to all players
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(
                            ChatColor.RED + "Starting in",
                            ChatColor.YELLOW + String.valueOf(secondsLeft),
                            10, 20, 10
                    );
                }

                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every 20 ticks (1 second)

        return true;
    }
}
