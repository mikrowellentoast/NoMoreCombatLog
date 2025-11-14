package org.mikrowellentoast.NoMoreCombatLog;

//import dev.jorel.commandapi.CommandAPI;
//import dev.jorel.commandapi.CommandAPIPaperConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.mikrowellentoast.NoMoreCombatLog.events.ConfigReloadEvent;
import org.mikrowellentoast.NoMoreCombatLog.listeners.CombatListener;
import org.mikrowellentoast.NoMoreCombatLog.listeners.ReloadListener;
//import org.mikrowellentoast.NoMoreCombatLog.commands.CommandRegister;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class NoMoreCombatLog extends JavaPlugin {
    @Override
    public void onLoad() {
        //CommandAPI.onLoad(new CommandAPIPaperConfig(this).verboseOutput(true));

        System.out.println("[NoMoreCombatLog] CommandAPI loaded.");

    }

    private static NoMoreCombatLog instance;



    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        //CommandAPI.onEnable();


        try {
            FileConfiguration defaults = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(getResource("config.yml"), StandardCharsets.UTF_8)
            );
            getConfig().addDefaults(defaults);
            getConfig().options().copyDefaults(true);
            saveConfig();
        } catch (Exception e) {
            getLogger().warning("Failed to merge default config: " + e.getMessage());
        }

        Bukkit.getPluginManager().registerEvents(new CombatListener(), this);
        Bukkit.getPluginManager().registerEvents(new ReloadListener(), this);





        //CommandRegister.register(this);


        getLogger().info("NoMoreCombatLog has been enabled.");

    }

    @Override
    public void onDisable() {
        //CommandAPI.onDisable();
        //getLogger().info("NoMoreCombatLog has been disabled.");
    }

    public static NoMoreCombatLog getInstance() {
        return instance;
    }

    public void reloadPluginConfig() {
        instance.reloadConfig();
        Bukkit.getPluginManager().callEvent(new ConfigReloadEvent());
    }
}
