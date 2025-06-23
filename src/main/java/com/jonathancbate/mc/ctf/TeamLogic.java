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

public class TeamLogic implements Listener {
    public void  teamCompile() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location loc = player.getLocation();
            Block blockUnder = loc.getBlock().getRelative(0, -1, 0);
            if (blockUnder.getType() == Material.RED_CONCRETE) {

            }
        }
    }
}
