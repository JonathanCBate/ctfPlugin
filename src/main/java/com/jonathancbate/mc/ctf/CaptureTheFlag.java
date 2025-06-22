package com.jonathancbate.mc.ctf;

import org.bukkit.plugin.java.JavaPlugin;

public class CaptureTheFlag extends JavaPlugin {
    public void onEnable() {
        getLogger().info("onEnable is called!");
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
