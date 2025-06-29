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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

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




    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /crate");
            return false;
        }
        else {
            World world = Bukkit.getWorld("world");
            if (world == null) {
                Bukkit.getLogger().warning("World 'world' not found!");
                return false;
            }
            Location location = getSafeRandomLocation(world, 0, 0, 0);
            return true;
        }
    }

    public ItemStack itemFromString() {
        Random random = new Random();
        int roll = random.nextInt(100) + 1;  // 1 to 100

        if (roll <= 5) {
            // 5% chance
            return new ItemStack(Material.NETHERITE_INGOT, 1);
        } else if (roll <= 10) {
            // next 5% (5% + 5%)
            return new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1);
        } else if (roll <= 20) {
            // next 10%
            return new ItemStack(Material.DIAMOND, random.nextInt(54) + 10);
        } else if (roll <= 25) {
            // next 5%
            return new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, random.nextInt(20)+ 5);
        } else if (roll <= 30) {
           return randomNetheriteItem();
        } else if(roll <= 35) {
            ItemStack mace = new ItemStack(Material.MACE, 1);
            mace.addEnchantment(Enchantment.BREACH , 5);
            mace.addEnchantment(Enchantment.UNBREAKING, 3);
            mace.addEnchantment(Enchantment.WIND_BURST, 3);
            mace.addEnchantment(Enchantment.MENDING, 1);
            return mace;
        } else if (roll <= 50) {
            return new ItemStack (Material.WIND_CHARGE, 64);
        } else if (roll <= 55) {
           return new ItemStack(Material.ENDER_PEARL, 16);
        } else if (roll <= 60) {
            ItemStack bow = new ItemStack(Material.BOW, 1);
            bow.addEnchantment(Enchantment.POWER, 5);
            bow.addEnchantment(Enchantment.PUNCH, 2);
            bow.addEnchantment(Enchantment.UNBREAKING, 3);
            bow.addEnchantment(Enchantment.MENDING, 1);
        } else if (roll <= 65) {
            return new ItemStack(Material.ARROW, 64);
        } else if (roll <= 70) {
           return new ItemStack(Material.OBSIDIAN, 64);
        } else if (roll <= 75) {
            return new ItemStack(Material.END_CRYSTAL, 64);
        } else if (roll <= 80) {
            return new ItemStack(Material.TOTEM_OF_UNDYING);
        } else if (roll <= 85) {
            return new ItemStack (Material.RESPAWN_ANCHOR,64);
        } else if (roll <= 90) {
            return new ItemStack (Material.GLOWSTONE);
        } else if (roll <= 95) {
            return new ItemStack (Material.GOLDEN_CARROT, 64);
        } else if (roll <= 99) {
            return randomWildcardItem();
        }
        else {
            return randomLegendaryItem();
        }
        // Default case, should never happen
        return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    }
    public ItemStack randomNetheriteItem() {
        Material[] netheriteItems = {
                Material.NETHERITE_SWORD,
                Material.NETHERITE_AXE,
                Material.NETHERITE_PICKAXE,
                Material.NETHERITE_HELMET,
                Material.NETHERITE_CHESTPLATE,
                Material.NETHERITE_LEGGINGS,
                Material.NETHERITE_BOOTS
        };

        int index = random.nextInt(netheriteItems.length);
        Material mat = netheriteItems[index];
        ItemStack item = new ItemStack(mat, 1);

        // Add specific enchants
        switch (mat) {
            case NETHERITE_SWORD:
                item.addEnchantment(Enchantment.SHARPNESS, 5);
                item.addEnchantment(Enchantment.UNBREAKING, 3);
                item.addEnchantment(Enchantment.LOOTING, 3);
                item.addEnchantment(Enchantment.FIRE_ASPECT, 2);
                item.addEnchantment(Enchantment.MENDING, 1);
                break;
            case NETHERITE_AXE:
                item.addEnchantment(Enchantment.SHARPNESS, 5);
                item.addEnchantment(Enchantment.EFFICIENCY, 5);
                item.addEnchantment(Enchantment.UNBREAKING, 3);
                item.addEnchantment(Enchantment.FORTUNE, 3);
                item.addEnchantment(Enchantment.SILK_TOUCH, 1);
                item.addEnchantment(Enchantment.MENDING, 1);
                break;
            case NETHERITE_PICKAXE:
                item.addEnchantment(Enchantment.EFFICIENCY, 5);
                item.addEnchantment(Enchantment.FORTUNE, 3);
                item.addEnchantment(Enchantment.MENDING, 1);
                item.addEnchantment(Enchantment.UNBREAKING, 3);
                break;
            case NETHERITE_HELMET:
                item.addEnchantment(Enchantment.PROTECTION, 4);
                item.addEnchantment(Enchantment.UNBREAKING, 3);
                item.addEnchantment(Enchantment.AQUA_AFFINITY, 1);
                item.addEnchantment(Enchantment.RESPIRATION, 3);
                break;
            case NETHERITE_CHESTPLATE:
                item.addEnchantment(Enchantment.PROTECTION, 4);
                item.addEnchantment(Enchantment.UNBREAKING, 3);
                item.addEnchantment(Enchantment.MENDING, 1);
                break;
            case NETHERITE_LEGGINGS:
                item.addEnchantment(Enchantment.PROTECTION, 4);
                item.addEnchantment(Enchantment.UNBREAKING, 3);
                item.addEnchantment(Enchantment.MENDING, 1);
                item.addEnchantment(Enchantment.SWIFT_SNEAK, 3);
                break;
            case NETHERITE_BOOTS:
                item.addEnchantment(Enchantment.PROTECTION, 4);
                item.addEnchantment(Enchantment.UNBREAKING, 3);
                item.addEnchantment(Enchantment.DEPTH_STRIDER, 3);
                item.addEnchantment(Enchantment.SOUL_SPEED, 3);
                item.addEnchantment(Enchantment.MENDING, 1);
                item.addEnchantment(Enchantment.FEATHER_FALLING, 4);
                break;
        }

        return item;
    }


}
