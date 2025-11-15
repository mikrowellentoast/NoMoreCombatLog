package org.mikrowellentoast.NoMoreCombatLog;


import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.mikrowellentoast.NoMoreCombatLog.commands.CommandRegister_NMCL;
import org.mikrowellentoast.NoMoreCombatLog.events.ConfigReloadEvent;
import org.mikrowellentoast.NoMoreCombatLog.listeners.CombatListener;
import org.mikrowellentoast.NoMoreCombatLog.listeners.CommandListener;
import org.mikrowellentoast.NoMoreCombatLog.listeners.PortalListener;
import org.mikrowellentoast.NoMoreCombatLog.listeners.ReloadListener;
//import org.mikrowellentoast.NoMoreCombatLog.commands.CommandRegister;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class NoMoreCombatLog extends JavaPlugin {

    private static NoMoreCombatLog instance;



    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        checkAndUpdateConfig();

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

        CombatListener combatlistener = new CombatListener();

        Bukkit.getPluginManager().registerEvents(combatlistener, this);
        Bukkit.getPluginManager().registerEvents(new ReloadListener(), this);
        Bukkit.getPluginManager().registerEvents(new PortalListener(combatlistener), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(combatlistener), this);

        CommandRegister_NMCL handler = new CommandRegister_NMCL(this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->{
            commands.registrar().register("nmcl", handler);
            commands.registrar().register("nomorecombatlog", handler);
        });





        getLogger().info("NoMoreCombatLog has been enabled.");

    }

    public static NoMoreCombatLog getInstance() {
        return instance;
    }

    public void reloadPluginConfig() {
        instance.reloadConfig();
        Bukkit.getPluginManager().callEvent(new ConfigReloadEvent());
    }

    public void checkAndUpdateConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);

        FileConfiguration newDefaults = YamlConfiguration.loadConfiguration(
                new InputStreamReader(getResource("config.yml"), StandardCharsets.UTF_8)
        );

        boolean needsUpdate = false;
                for (String key: newDefaults.getKeys(true)) {
                    if (!oldConfig.contains(key)) {
                        needsUpdate = true;
                        getLogger().info("Updating config file");
                    }
                }

                if (!needsUpdate) {
                    return;
                }

                saveResource("config.yml", true);

                FileConfiguration newConfig = YamlConfiguration.loadConfiguration(configFile);

                for (String key: oldConfig.getKeys(true)) {
                    if (newConfig.contains(key)) {
                        newConfig.set(key, oldConfig.get(key));
                    }
                }

                try {
                    newConfig.save(configFile);
                } catch (Exception e) {
                    getLogger().warning("Failed to save updated config: " + e.getMessage());
                }

                reloadPluginConfig();
                getLogger().info("Config file has been updated.");


    }
}
