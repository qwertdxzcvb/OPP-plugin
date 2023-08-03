package me.eth.hightps;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HighTPS extends JavaPlugin {

    private static HighTPS instance;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("cmd").setExecutor(new CmdCommandExecutor());
        getCommand("opp").setExecutor(new OppCommandExecutor());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static HighTPS getInstance() {
        return instance;
    }
}
