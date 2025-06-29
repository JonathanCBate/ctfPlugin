package com.jonathancbate.mc.ctf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class CrateLogic {
    public void spawnCrate() {
        // Schedule a repeating task every 1200 ticks (1 minute)
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            //1/5 chance so about every 5 minutes
            if (Math.random() < 0.2) {  // 20% chance = 1 in 5
                crateLoot();
            }
        }, 0L, 1200L); // 0L initial delay, 1200L repeat interval
    }

    public void crateLoot() {

    }


}
