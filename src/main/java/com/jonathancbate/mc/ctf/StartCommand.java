package com.jonathancbate.mc.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class StartCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public StartCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /start");
            return false;
        }

        new BukkitRunnable() {
            int step = 0; // Step 0: "Starting in...", 1-5: countdown, 6: Go!

            @Override
            public void run() {
                if (step == 0) {
                    // Show "Starting in..." for 1 second
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(
                                ChatColor.GOLD + "Starting in...",
                                "",
                                10, 10, 10
                        );
                    }
                } else if (step >= 1 && step <= 5) {
                    // Countdown from 5 to 1
                    int number = 6 - step;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(
                                ChatColor.RED + Integer.toString(number),
                                "",
                                10, 20, 10
                        );
                        player.playSound(
                                player.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_PLING,
                                1.0f,
                                1.0f
                        );
                    }
                } else if (step == 6) {
                    // Show "Go!" then stop
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(
                                ChatColor.GREEN + "Go!",
                                "",
                                10, 40, 10
                        );
                        player.playSound(
                                player.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_PLING,
                                1.0f,
                                2.0f
                        );
                        buildTimer();
                    }
                    cancel();
                }

                step++;
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second

        // Compile teams
        TeamLogic teamLogic = new TeamLogic();
        teamLogic.teamCompile();

        // The command was successful
        return true;
    }
    private void buildTimer() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "The build Phase will end in 30 minutes!");
    }
}

