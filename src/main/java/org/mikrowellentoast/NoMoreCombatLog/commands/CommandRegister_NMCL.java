package org.mikrowellentoast.NoMoreCombatLog.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.mikrowellentoast.NoMoreCombatLog.NoMoreCombatLog;
import org.mikrowellentoast.NoMoreCombatLog.commands.subcommands.*;

import java.util.*;


public class CommandRegister_NMCL implements BasicCommand {

    private final Map<String, Subcommand> subcommands = new HashMap<>();

    private final NoMoreCombatLog plugin;

    public CommandRegister_NMCL(NoMoreCombatLog plugin) {
        this.plugin = plugin;
        register(new reloadcommand(plugin));
    }

    public void register(Subcommand cmd) {
        subcommands.put(cmd.getName(), cmd);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission("nomorecombatlog.use");
    }


    @Override
    public void execute(CommandSourceStack stack, String[] args) {

        CommandSender sender = stack.getSender();

        if (args.length == 0) {
            sender.sendMessage("NoMoreCombatLog version §a" + plugin.getDescription().getVersion());
            return;
        }

        Subcommand cmd = subcommands.get(args[0]);
        if (cmd == null) {
            sender.sendMessage("§cUnknown subcommand: " + args[0]);
            return;
        }

        if (!sender.hasPermission(cmd.getPermission())) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return;
        }

        cmd.execute(stack, Arrays.copyOfRange(args, 1, args.length));
    }



    @Override
    public List<String> suggest(CommandSourceStack stack, String[] args) {
        if (args.length == 0) {
            return new ArrayList<>(subcommands.keySet());
        }

        if (args.length == 1) {
            return subcommands.keySet().stream()
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        Subcommand cmd = subcommands.get(args[0].toLowerCase());
        if (cmd != null) {
            return cmd.suggest(stack, args);
        }
        return List.of();
    }
}
