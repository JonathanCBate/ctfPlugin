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
    public static final List<UUID> redTeam = new ArrayList<>();
    public static final List<UUID> blueTeam = new ArrayList<>();

    public static UUID redTeamLeader = null;
    public static UUID blueTeamLeader = null;

    protected void teamCompile() {
        redTeam.clear();
        blueTeam.clear();
        redTeamLeader = null;
        blueTeamLeader = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location loc = player.getLocation();
            Block blockUnder = loc.getBlock().getRelative(0, -1, 0);
            UUID playerUUID = player.getUniqueId();

            if (blockUnder.getType() == Material.RED_CONCRETE) {
                redTeam.add(playerUUID);
                player.sendMessage(ChatColor.RED + "You are on the Red Team!");
            } else if (blockUnder.getType() == Material.RED_WOOL) {
                redTeam.add(playerUUID);
                redTeamLeader = playerUUID;
                player.sendMessage(ChatColor.RED + "You are the Red Team Leader!");
            } else if (blockUnder.getType() == Material.BLUE_CONCRETE) {
                blueTeam.add(playerUUID);
                player.sendMessage(ChatColor.BLUE + "You are on the Blue Team!");
            } else if (blockUnder.getType() == Material.BLUE_WOOL) {
                blueTeam.add(playerUUID);
                blueTeamLeader = playerUUID;
                player.sendMessage(ChatColor.BLUE + "You are the Blue Team Leader!");
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player damaged && event.getDamager() instanceof Player damager) {
            UUID damagedUUID = damaged.getUniqueId();
            UUID damagerUUID = damager.getUniqueId();

            if (redTeam.contains(damagedUUID) && redTeam.contains(damagerUUID)) {
                event.setCancelled(true);
            } else if (blueTeam.contains(damagedUUID) && blueTeam.contains(damagerUUID)) {
                event.setCancelled(true);
            } else if (!StartCommand.pvp) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();

        event.setCancelled(true); // cancel default broadcast

        UUID senderUUID = sender.getUniqueId();

        for (Player recipient : Bukkit.getOnlinePlayers()) {
            UUID recipientUUID = recipient.getUniqueId();
            String teamPrefix = "";

            if ((redTeam.contains(senderUUID) && redTeam.contains(recipientUUID)) ||
                    (blueTeam.contains(senderUUID) && blueTeam.contains(recipientUUID))) {
                teamPrefix = ChatColor.GREEN + "[Team] ";
            } else if (redTeam.contains(senderUUID)) {
                teamPrefix = ChatColor.RED + "[Red] ";
            } else if (blueTeam.contains(senderUUID)) {
                teamPrefix = ChatColor.BLUE + "[Blue] ";
            }

            System.out.println("Sender: " + sender.getName());
            System.out.println("Sender in redTeam? " + redTeam.contains(senderUUID));
            System.out.println("Sender in blueTeam? " + blueTeam.contains(senderUUID));

            recipient.sendMessage(teamPrefix + sender.getName() + ChatColor.RESET + ": " + message);
        }
    }

    protected void teamReset() {
        redTeam.clear();
        blueTeam.clear();
        redTeamLeader = null;
        blueTeamLeader = null;
    }
}
