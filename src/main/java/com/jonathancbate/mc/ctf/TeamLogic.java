package com.jonathancbate.mc.ctf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamLogic implements Listener {
    // Use UUID lists instead of Player lists
    public static final List<String> redTeam = new ArrayList<>();
    public static final List<String> blueTeam = new ArrayList<>();

    public static String redTeamLeader = null;
    public static String blueTeamLeader = null;

    protected void teamCompile() {
        redTeam.clear();
        blueTeam.clear();
        redTeamLeader = null;
        blueTeamLeader = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location loc = player.getLocation();
            Block blockUnder = loc.getBlock().getRelative(0, -1, 0);
            String playerName = player.getName();

            if (blockUnder.getType() == Material.RED_CONCRETE) {
                redTeam.add(playerName);
                player.sendMessage(ChatColor.RED + "You are on the Red Team!");
            } else if (blockUnder.getType() == Material.RED_WOOL) {
                redTeam.add(playerName);
                redTeamLeader = playerName;
                player.sendMessage(ChatColor.RED + "You are the Red Team Leader!");
            } else if (blockUnder.getType() == Material.BLUE_CONCRETE) {
                blueTeam.add(playerName);
                player.sendMessage(ChatColor.BLUE + "You are on the Blue Team!");
            } else if (blockUnder.getType() == Material.BLUE_WOOL) {
                blueTeam.add(playerName);
                blueTeamLeader = playerName;
                player.sendMessage(ChatColor.BLUE + "You are the Blue Team Leader!");
            }
        }

        System.out.println("=== RED TEAM MEMBERS ===");
        for (String name : redTeam) {
            System.out.println(name);
        }
        System.out.println("=== BLUE TEAM MEMBERS ===");
        for (String name : blueTeam) {
            System.out.println(name);
        }
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player damaged && event.getDamager() instanceof Player damager) {
            String damagedPlayer = damaged.getName();
            String damagerPlayer = damager.getName();

            if (redTeam.contains(damagedPlayer) && redTeam.contains(damagerPlayer)) {
                event.setCancelled(true);
            } else if (blueTeam.contains(damagedPlayer) && blueTeam.contains(damagerPlayer)) {
                event.setCancelled(true);
            } else if (!StartCommand.pvp) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String senderName = sender.getName();
        String message = event.getMessage();

        event.setCancelled(true); // Cancel default broadcast

        for (Player recipient : Bukkit.getOnlinePlayers()) {
            String recipientName = recipient.getName();
            String teamPrefix = "";

            boolean senderOnRed = redTeam.contains(senderName);
            boolean senderOnBlue = blueTeam.contains(senderName);

            boolean recipientOnRed = redTeam.contains(recipientName);
            boolean recipientOnBlue = blueTeam.contains(recipientName);

            boolean senderIsRedLeader = senderName.equals(redTeamLeader);
            boolean senderIsBlueLeader = senderName.equals(blueTeamLeader);

            // Check if sender and recipient are on the same team
            if ((senderOnRed && recipientOnRed) || (senderOnBlue && recipientOnBlue)) {
                teamPrefix = ChatColor.GREEN + "[Team] ";
            } else if (senderOnRed) {
                if (senderIsRedLeader) {
                    teamPrefix = ChatColor.YELLOW + "[Red Leader] ";
                } else {
                    teamPrefix = ChatColor.RED + "[Red] ";
                }
            } else if (senderOnBlue) {
                if (senderIsBlueLeader) {
                    teamPrefix = ChatColor.YELLOW + "[Blue Leader] ";
                } else {
                    teamPrefix = ChatColor.BLUE + "[Blue] ";
                }
            }

            recipient.sendMessage(teamPrefix + senderName + ChatColor.RESET + ": " + message);
        }
    }



    protected void teamReset() {
        redTeam.clear();
        blueTeam.clear();
        redTeamLeader = null;
        blueTeamLeader = null;
    }
}
