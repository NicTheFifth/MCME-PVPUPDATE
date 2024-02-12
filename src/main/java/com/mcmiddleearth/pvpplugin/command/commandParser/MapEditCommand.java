package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.command.executor.EditExecutor;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MapEditCommand extends AbstractCommandHandler implements TabExecutor {

    public MapEditCommand(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(
                CommandUtil.multiRequirements(Requirements::isMapEditor,
                    (sender -> ((PVPCommandSender)sender).getSender() instanceof Player)))
            .then(
                HelpfulLiteralBuilder.literal("create")
                    .then(Arguments.NonExistingMap()
                        .executes(EditExecutor::CreateMap)))
            .then(
                ActiveMapEditorLiteral("rename")
                    .then(Arguments.NonExistingMap()
                        .executes(EditExecutor::SetTitle)))
            .then(
                HelpfulLiteralBuilder.literal("select")
                    .then(Arguments.ExistingMap()
                        .executes(EditExecutor::SelectMap)))
            .then(
                ActiveMapEditorLiteral("setarea")
                    .executes(EditExecutor::SetArea))
            .then(
                ActiveMapEditorLiteral("setrp")
                    .then(Arguments.RPArgument()
                        .executes(EditExecutor::SetRP)))
            .then(
                ActiveMapEditorLiteral("setmapspawn")
                    .executes(EditExecutor::setMapSpawn))
            .then(
                ActiveMapEditorLiteral("gamemode")
                    .then(Arguments.GetGamemodes()
                        .executes(EditExecutor::SetGamemode)))
            .then(ActiveGamemodeEditorLiteral("setmax")
                .then(HelpfulRequiredArgumentBuilder.argument(ArgumentNames.MAX, IntegerArgumentType.integer(0))
                    .executes(EditExecutor::SetMax)))
            .then(ActiveGamemodeEditorLiteral("spawn")
                .then(HelpfulLiteralBuilder.literal("add")
                    .requires(Requirements::instanceOfSpawnListEditor)
                    .executes(EditExecutor::SpawnListAddSpawn))
                .then(HelpfulLiteralBuilder.literal("delete")
                    .requires(Requirements::instanceOfSpawnListEditor)
                    .then(Arguments.spawnIndexArgument()
                        .executes(EditExecutor::SpawnListDeleteSpawn)))
                .then(Arguments.spawnNameArgument()
                    .requires(Requirements::NotInstanceOfSpawnListEditor)
                    .then(HelpfulLiteralBuilder.literal("set")
                        .requires(Requirements::InstanceOfTeamSpawnEditor)
                        .executes(EditExecutor::TeamSpawnSetSpawn))
                    .then(HelpfulLiteralBuilder.literal("add")
                        .requires(Requirements::InstanceOfTeamSpawnListEditor)
                        .executes(EditExecutor::TeamSpawnListAddSpawn))
                    .then(HelpfulLiteralBuilder.literal("delete")
                        .requires(Requirements::InstanceOfTeamSpawnListEditor)
                        .then(Arguments.spawnIndexArgument()
                            .executes(EditExecutor::TeamSpawnListDeleteSpawn)))))
            .then(Arguments.SpecialPointArgument()
                .requires(Requirements::hasSpecialPoints)
                .then(HelpfulLiteralBuilder.literal("set")
                    .requires(Requirements::isInstanceOfSpecialPointEditor)
                    .executes(EditExecutor::SetSpecialPoint))
                .then(HelpfulLiteralBuilder.literal("add")
                    .requires(Requirements::isInstanceOfSpecialPointListEditor)
                    .executes(EditExecutor::AddSpecialPoint))
                .then(HelpfulLiteralBuilder.literal("delete")
                    .requires(Requirements::isInstanceOfSpecialPointListEditor)
                    .then(Arguments.SpecialPointListIndexArgument()
                        .executes(EditExecutor::DeleteSpecialPoint))))
            .then(ActiveMapEditorLiteral("info")
                .executes(EditExecutor::SendInfo)
                .then(ActiveGamemodeEditorLiteral("gamemode")
                    .executes(EditExecutor::SendGamemodeInfo)))
            ;

        return commandNodeBuilder;
    }

    private LiteralArgumentBuilder<McmeCommandSender> ActiveMapEditorLiteral(String literal){
        return HelpfulLiteralBuilder.literal(literal)
            .requires(Requirements::hasActiveMapEditor);
    }
    private LiteralArgumentBuilder<McmeCommandSender> ActiveGamemodeEditorLiteral(String literal){
        return HelpfulLiteralBuilder.literal(literal)
            .requires(Requirements::hasActiveGamemodeEditor);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String[] args) {
        PVPCommandSender wrappedSender = new PVPCommandSender(sender);
        execute(wrappedSender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(PVPCommandSender.wrap(sender),
                String.format("/%s %s", alias, Joiner.on(' ').join(args)));
        onTabComplete(request);
        return request.getSuggestions();
    }
}
