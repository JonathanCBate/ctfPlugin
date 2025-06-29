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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.minecart.StorageMinecart;

import java.util.Random;

public class CrateLogic {
    private final JavaPlugin plugin;
    private final Random random = new Random();

    public CrateLogic(JavaPlugin plugin) {
        this.plugin = plugin;
        spawnCrate();
    }

    public void spawnCrate() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (random.nextDouble() < 0.2) {
                crateLoot();
            }
        }, 0L, 1200L);
    }
    public void crateLoot() {
        World world = Bukkit.getWorld("world");
        if (world == null) {
            Bukkit.getLogger().warning("World 'world' not found!");
            return;
        }
        Location location = getSafeRandomLocation(world, 0, 0, 500);
        if (location != null) {
            StorageMinecart minecart = location.getWorld().spawn(location, StorageMinecart.class);

            // Give it a special tag you can check later
            NamespacedKey key = new NamespacedKey(plugin, "loot_crate");
            minecart.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            minecart.setCustomName(ChatColor.GOLD + "Loot Crate");
            minecart.setCustomNameVisible(true);
            Bukkit.broadcastMessage(ChatColor.GOLD + "A crate has spawned at X: "
                    + location.getBlockX() + ", Z: " + location.getBlockZ() + "!");

        }else {
            Bukkit.getLogger().warning("Failed to find a safe location for the loot crate.");
        }
    }
    private Location getSafeRandomLocation(World world, int centerX, int centerZ, int radius) {
        for (int attempts = 0; attempts < 20; attempts++) {  // Try up to 20 random spots
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = random.nextDouble() * radius;
            int x = centerX + (int) (distance * Math.cos(angle));
            int z = centerZ + (int) (distance * Math.sin(angle));

            int y = world.getHighestBlockYAt(x, z) - 1;  // Get block at surface
            if (y <= 0) continue;

            Material blockType = world.getBlockAt(x, y, z).getType();

            if (blockType.isSolid() &&
                    blockType != Material.WATER &&
                    blockType != Material.LAVA &&
                    blockType != Material.AIR) {
                return new Location(world, x + 0.5, y + 1, z + 0.5);  // Spawn 1 above ground
            }
        }
        return null;  // Failed to find safe location
    }


}
