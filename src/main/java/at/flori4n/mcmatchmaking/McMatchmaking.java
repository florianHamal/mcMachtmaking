package at.flori4n.mcmatchmaking;

import at.flori4n.mcmatchmaking.commands.SetupCommands;
import at.flori4n.mcmatchmaking.commands.StartCommand;
import at.flori4n.mcmatchmaking.commands.TeamCommands;
import at.flori4n.mcmatchmaking.gameData.GameData;
import at.flori4n.mcmatchmaking.gameManager.Manager;
import at.flori4n.mcmatchmaking.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class McMatchmaking extends JavaPlugin {

    private static McMatchmaking plugin;
    @Override
    public void onEnable() {
        plugin=this;
        Manager.getInstance().start();
        Bukkit.broadcastMessage("test");
        getCommand("mcMatchmaking").setExecutor(new SetupCommands());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("team").setExecutor(new TeamCommands());
        PluginManager pluginManager =  Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinListener(),this);
        pluginManager.registerEvents(new FoodLvlListerner(),this);
        pluginManager.registerEvents(new QuitListener(),this);
        pluginManager.registerEvents(new DeathListener(),this);
        pluginManager.registerEvents(new HitListener(),this);
        pluginManager.registerEvents(new Gui(),this);
    }
    public static McMatchmaking getPlugin() {
        return plugin;
    }
}
