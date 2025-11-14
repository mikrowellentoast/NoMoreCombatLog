package org.mikrowellentoast.NoMoreCombatLog.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mikrowellentoast.NoMoreCombatLog.NoMoreCombatLog;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mikrowellentoast.NoMoreCombatLog.events.ConfigReloadEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class CombatListener implements Listener {

    private final HashMap<UUID, Long> combatTagged = new HashMap<>();
    private final NoMoreCombatLog plugin = NoMoreCombatLog.getInstance();
    private long COMBAT_TAG_DURATION;
    private boolean ENABLED_IN_CREATIVE;
    private boolean PLUGIN_ENABLED;
    private int taskId = -1;

    public CombatListener() {
       this.COMBAT_TAG_DURATION = plugin.getConfig().getLong("combat-tag-duration", 15) * 1000;
       this.ENABLED_IN_CREATIVE = plugin.getConfig().getBoolean("enable-in-creative", false);
       this.PLUGIN_ENABLED = plugin.getConfig().getBoolean("enabled", true);
       startActionbarTask();
    }


    @EventHandler
    public void onCombat(EntityDamageByEntityEvent event) {
        if (!PLUGIN_ENABLED) {
            return;
        }

        if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
            long now = System.currentTimeMillis();
            GameMode victimMode = victim.getGameMode();
            GameMode attackerMode = attacker.getGameMode();
            if (!victim.hasPermission("nomorecombatlog.bypass")) {
                if (!ENABLED_IN_CREATIVE && victimMode == GameMode.CREATIVE) {
                    return;
                }
                combatTagged.put(victim.getUniqueId(), now);
            }
            if (!attacker.hasPermission("nomorecombatlog.bypass")) {

                if (!ENABLED_IN_CREATIVE && attackerMode == GameMode.CREATIVE) {
                    return;
                }
                combatTagged.put(attacker.getUniqueId(), now);



            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (combatTagged.containsKey(player.getUniqueId())) {
            combatTagged.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        long now = System.currentTimeMillis();
        Player player = event.getPlayer();
        Long lastTagged = combatTagged.get(player.getUniqueId());

        long duration = COMBAT_TAG_DURATION;
        if (lastTagged != null && now - lastTagged < duration) {
            combatTagged.remove(player.getUniqueId());
            player.setHealth(0);
        }
    }

    @EventHandler
    private void onConfigReload(ConfigReloadEvent event) {

        this.COMBAT_TAG_DURATION = plugin.getConfig().getLong("combat-tag-duration", 15) * 1000;
        this.ENABLED_IN_CREATIVE = plugin.getConfig().getBoolean("enable-in-creative", false);
        this.PLUGIN_ENABLED = plugin.getConfig().getBoolean("enabled", true);
        startActionbarTask();
    }

    private void startActionbarTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        taskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long currentTime = System.currentTimeMillis();
            Iterator<Map.Entry<UUID, Long>> it = combatTagged.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<UUID, Long> entry = it.next();
                UUID uuid = entry.getKey();
                long lastCombat = entry.getValue();
                long elapsed = currentTime - lastCombat;
                long remaining = COMBAT_TAG_DURATION - elapsed;
                Player player = Bukkit.getPlayer(uuid);

                if (remaining <= 0) {
                    it.remove();
                    player.sendActionBar("§cYou're no longer in combat.");
                    continue;
                }

                ;
                if (player != null && player.isOnline()) {
                    long seconds = (remaining + 999) / 1000;
                    player.sendActionBar("§cCombat: §e" + seconds + "s");
                }
            }
        }, 0L, 20L).getTaskId(); // läuft jede Sekunde
    }



    }

