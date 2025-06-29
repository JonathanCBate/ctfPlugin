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
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;



public class StartCommand implements CommandExecutor {
    public static boolean pvp = false;
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
                                Sound.ENTITY_ENDER_DRAGON_GROWL,
                                1.0f,
                                1.0f
                        );
                        buildTimer(plugin, () -> {}, () -> {});
                        // Compile teams
                        TeamLogic teamLogic = new TeamLogic();
                        teamLogic.teamReset();
                        teamLogic.teamCompile();

                        pvp = false;
                    }
                    cancel();
                }

                step++;
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second



        // The command was successful
        return true;
    }
    private void buildTimer(JavaPlugin plugin, Runnable onTick, Runnable onEnd) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "The build phase will end in 30 minutes!");

        new BukkitRunnable() {
            int time = 0; // Start counting up from 0

            @Override
            public void run() {
                switch (time) {
                    case 900:
                        brodcastMessage("15 minutes remaining!", ChatColor.YELLOW, 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1200:
                        brodcastMessage("10 minutes remaining!", ChatColor.GOLD, 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1500:
                        brodcastMessage("5 minutes remaining!", ChatColor.RED, 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1740:
                        brodcastMessage("1 minute remaining!", ChatColor.RED, 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1770:
                        brodcastMessage("30 seconds remaining!", ChatColor.RED, 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1790:
                        brodcastMessage("10 seconds remaining!", ChatColor.RED, 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1795:
                        brodcastMessage("Starting in 5", ChatColor.DARK_RED, 2);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1796:
                        brodcastMessage("4", ChatColor.DARK_RED, 2);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1797:
                        brodcastMessage("3", ChatColor.DARK_RED, 2);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1798:
                        brodcastMessage("2", ChatColor.DARK_RED, 2);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1799:
                        brodcastMessage("1", ChatColor.DARK_RED, 2);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        }
                        break;
                    case 1800:
                        brodcastMessage("Time's up! PvP is now enabled!", ChatColor.RED, 2);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                        }
                        pvp = true;
                        onEnd.run(); // Call the end logic
                        cancel();
                        return;
                }

                onTick.run(); // optional user-supplied tick logic
                time++;
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second
    }


    private void brodcastMessage(String message, ChatColor chatColor, int chatType) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            switch (chatType) {
                case 1: // Action Bar
                    player.spigot().sendMessage(
                            net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                            new net.md_5.bungee.api.chat.TextComponent(chatColor + message)
                    );
                    break;

                case 2: // Title
                    player.sendTitle(chatColor + message, "", 10, 70, 20); // fadeIn, stay, fadeOut
                    break;

                case 3: // Chat message
                default:
                    player.sendMessage(chatColor + message);
                    break;
            }
        }
    }
}

