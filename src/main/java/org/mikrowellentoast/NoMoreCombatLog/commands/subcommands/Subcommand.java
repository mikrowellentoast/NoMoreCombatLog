package org.mikrowellentoast.NoMoreCombatLog.commands.subcommands;

import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.List;

public interface Subcommand {

    void execute(CommandSourceStack stack, String[] args);

    default List<String> suggest(CommandSourceStack stack, String[] args) {
        return List.of();
    }

    String getName();

    String getPermission();
}