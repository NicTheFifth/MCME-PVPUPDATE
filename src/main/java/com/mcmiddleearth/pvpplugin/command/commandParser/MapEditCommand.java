package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.command.executor.EditExecutor;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapEditCommand extends AbstractCommandHandler implements TabExecutor {

    public MapEditCommand(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(Requirements::isMapEditor)
            .requires(sender -> ((PVPCommandSender)sender).getSender() instanceof Player)
            .then(
                HelpfulLiteralBuilder.literal("create")
                    .then(Arguments.NonExistingMap()
                        .executes(EditExecutor::CreateMap)))
            .then(
                ActiveGamemodeLiteral("rename")
                    .then(Arguments.NonExistingMap()
                        .executes(EditExecutor::SetTitle)))
            .then(
                HelpfulLiteralBuilder.literal("select")
                    .then(Arguments.ExistingMap()
                        .executes(EditExecutor::SelectMap)))
            .then(
                ActiveGamemodeLiteral("setarea")
                    .executes(EditExecutor::SetArea))
            .then(
                ActiveGamemodeLiteral("setrp")
                    .then(Arguments.RPArgument()
                        .executes(EditExecutor::SetRP)))
            .then(
                ActiveGamemodeLiteral("mapspawn")
                    .executes(EditExecutor::setMapSpawn))
            .then(
                ActiveGamemodeLiteral("gamemode")
                    .then(Arguments.GetGamemodes()
                        .executes(EditExecutor::SetGamemode)))
            ;

        return commandNodeBuilder;
    }

    private LiteralArgumentBuilder<McmeCommandSender> ActiveGamemodeLiteral(String literal){
        return HelpfulLiteralBuilder.literal(literal)
            .requires(Requirements::hasActiveGamemodeEditor);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Logger.getLogger("PVPPlugin").log(Level.INFO,
            String.join(",", args));
        PVPCommandSender wrappedSender = new PVPCommandSender(sender);
        execute(wrappedSender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(PVPCommandSender.wrap(sender),
                String.format("/%s %s", alias, Joiner.on(' ').join(args)));
        onTabComplete(request);
        return request.getSuggestions();
    }
}
