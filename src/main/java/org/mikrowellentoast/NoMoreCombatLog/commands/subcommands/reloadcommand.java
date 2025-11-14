package org.mikrowellentoast.NoMoreCombatLog.commands.subcommands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.mikrowellentoast.NoMoreCombatLog.NoMoreCombatLog;

public class reloadcommand implements Subcommand {
    private final NoMoreCombatLog plugin;

    public reloadcommand(NoMoreCombatLog plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack stack, String[] args) {
        CommandSender sender = stack.getSender();
        plugin.reloadPluginConfig();
        sender.sendMessage("§a[NoMoreCombatLog] configuration reloaded.");
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "nomorecombatlog.reload";
    }
}
