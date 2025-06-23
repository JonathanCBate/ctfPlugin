package com.jonathancbate.mc.ctf;

//Import necessary classes
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TeamLogic implements Listener {
    public static final List<Player> redTeam = new ArrayList<>();
    public static final List<Player> blueTeam = new ArrayList<>();
    public static Player redTeamLeader = null;
    public static Player blueTeamLeader = null;
    protected void  teamCompile() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location loc = player.getLocation();
            Block blockUnder = loc.getBlock().getRelative(0, -1, 0);
            if (blockUnder.getType() == Material.RED_CONCRETE) {
                redTeam.add(player);
                player.sendMessage(ChatColor.RED + "You are on the Red Team!");
            }else if (blockUnder.getType() == Material.RED_WOOL) {
                redTeam.add(player);
                redTeamLeader = player;
                player.sendMessage(ChatColor.RED + "You are the Red Team Leader!");
            }else if (blockUnder.getType() == Material.BLUE_CONCRETE) {
                blueTeam.add(player);
                player.sendMessage(ChatColor.BLUE + "You are on the Blue Team!");
            }else if (blockUnder.getType() == Material.BLUE_WOOL) {
                blueTeam.add(player);
                blueTeamLeader = player;
                player.sendMessage(ChatColor.BLUE + "You are the Blue Team Leader!");
            }
        }
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player damaged && event.getDamager() instanceof Player damager) {
            if (redTeam.contains(damaged) && redTeam.contains(damager)) {
                // Prevent damage between players on the same team
                event.setCancelled(true);
            } else if (blueTeam.contains(damaged) && blueTeam.contains(damager)) {
                // Prevent damage between players on the same team
                event.setCancelled(true);
            }else if (!StartCommand.pvp) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();

        event.setCancelled(true); // cancel default broadcast

        for (Player recipient : Bukkit.getOnlinePlayers()) {
            String teamPrefix = "";

            // Customize prefix based on recipient's team info
            if (TeamLogic.redTeam.contains(sender) && TeamLogic.redTeam.contains(recipient)) {
                teamPrefix = ChatColor.GREEN + "[Team] ";
            }

            // Check if both are on the blue team
            else if (TeamLogic.blueTeam.contains(sender) && TeamLogic.blueTeam.contains(recipient)) {
                teamPrefix = ChatColor.GREEN + "[Team] ";
            }
            else if (TeamLogic.redTeam.contains(sender) && !TeamLogic.blueTeam.contains(recipient)) {
                teamPrefix = ChatColor.RED + "[Red] ";
            } else if (TeamLogic.blueTeam.contains(sender) && !TeamLogic.redTeam.contains(recipient)) {
                teamPrefix = ChatColor.BLUE + "[Blue] ";
            }

            // You could add logic here so recipient sees different info
            recipient.sendMessage(teamPrefix + ChatColor.RESET + " " + sender.getName() + ": " + message);
        }
    }
}
