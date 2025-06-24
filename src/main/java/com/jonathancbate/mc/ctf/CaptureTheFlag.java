package com.jonathancbate.mc.ctf;

import org.bukkit.plugin.java.JavaPlugin;

public class CaptureTheFlag extends JavaPlugin {
    public void onEnable() {
        getLogger().info("The Capture The Flag plugin is enabled!");
        getCommand("start").setExecutor(new StartCommand(this));
        getServer().getPluginManager().registerEvents(new TeamLogic(), this);
    }
    @Override
    public void onDisable() {
        getLogger().info("The Capture The Flag plugin is disabled!");
    }
}
