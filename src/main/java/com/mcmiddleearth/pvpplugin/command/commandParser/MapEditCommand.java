package com.mcmiddleearth.pvpplugin.command.commandParser;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.pvpplugin.command.PVPCommandSender;
import com.mcmiddleearth.pvpplugin.command.executor.EditExecutor;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
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
            .requires(Requirements::isMapEditor)
            .requires(sender -> sender instanceof Player)
            .then(
                CreateNewMap())
            .then(
                RenameMap())
            .then(
                SelectMap())
            .then(
                SetArea())
            .then(
                SetRP())
            .then(
                SetMapSpawn()
                    .then(
                        SetRedBlueSpawn())
                    .then(
                        SetInfectedSurvivorSpawn())
                    .then(
                        SetRunnerDeathSpawn()
                    )
            )
            .then(
                AddSpawn())
            .then(
                DeleteSpawn())
            .then(
                SelectGamemode())
            .then(
                SetMax())
            .then(
                SetGoal())
            .then(
                SetCapture())
            .then(
                DeleteCapture())
            ;
        return commandNodeBuilder;
    }



    //<editor-fold defaultstate="collapsed" desc="Individual Commands">
    private LiteralArgumentBuilder<McmeCommandSender> SelectMap() {
        return HelpfulLiteralBuilder.literal("select")
                .then(Arguments.ExistingMap()
                        .executes(EditExecutor::SelectMap));
    }
    private LiteralArgumentBuilder<McmeCommandSender> CreateNewMap() {
        return HelpfulLiteralBuilder.literal("create")
                .then(Arguments.NonExistingMap()
                        .executes(EditExecutor::CreateMap));
    }
    private LiteralArgumentBuilder<McmeCommandSender> SetArea(){
        return HelpfulLiteralBuilder.literal("setarea")
                .executes(EditExecutor::SetArea);
    }
    private LiteralArgumentBuilder<McmeCommandSender> RenameMap() {
        return HelpfulLiteralBuilder.literal("rename")
                .then(Arguments.NonExistingMap()
                        .executes(EditExecutor::SetTitle));
    }
    private LiteralArgumentBuilder<McmeCommandSender> SetRP(){
        return HelpfulLiteralBuilder.literal("setrp")
                .then(Arguments.RPArgument()
                        .executes(EditExecutor::SetRP));
    }
    private LiteralArgumentBuilder<McmeCommandSender> SetMapSpawn(){
        return HelpfulLiteralBuilder.literal("setSpawn")
                .executes(EditExecutor::setMapSpawn);
    }
    private static HelpfulRequiredArgumentBuilder<String> SetRedBlueSpawn() {
        return Arguments.RedBlueSpawnArgument()
            .executes(EditExecutor::SetRedBlueSpawn);
    }
    private static HelpfulRequiredArgumentBuilder<String> SetInfectedSurvivorSpawn() {
        return Arguments.InfectedSurvivorSpawnArgument()
            .executes(EditExecutor::SetInfectedSurvivorSpawn);
    }
    private static HelpfulRequiredArgumentBuilder<String> SetRunnerDeathSpawn() {
        return Arguments.RunnerDeathSpawnArgument()
            .executes(EditExecutor::SetRunnerDeathSpawn);
    }
    private static LiteralArgumentBuilder<McmeCommandSender> AddSpawn() {
        return HelpfulLiteralBuilder.literal("addspawn")
            .requires(Requirements::canEditSpawnList)
            .executes(EditExecutor::AddSpawn);
    }
    private ArgumentBuilder<McmeCommandSender,?> DeleteSpawn() {
        return HelpfulLiteralBuilder.literal("deletespawn")
            .requires(Requirements::canEditSpawnList)
            .then(Arguments.GetSpawns()
                .executes(EditExecutor::DelSpawn));

    }
    private static LiteralArgumentBuilder<McmeCommandSender> SelectGamemode(){
        return HelpfulLiteralBuilder.literal("gamemode")
                .then(Arguments.GetGamemodes()
                        .executes(EditExecutor::SetGamemode));
    }
    private static LiteralArgumentBuilder<McmeCommandSender> SetMax(){
        return HelpfulLiteralBuilder.literal("setMax")
                .requires(Requirements::allGamemode)
                .then(HelpfulRequiredArgumentBuilder.argument("amount",IntegerArgumentType.integer(1))
                        .executes(EditExecutor::SetMax));
    }
    private static LiteralArgumentBuilder<McmeCommandSender> SetGoal(){
        return HelpfulLiteralBuilder.literal("setGoal")
                .requires(Requirements::canEditGoal)
                .executes(EditExecutor::EditGoal);
    }
    private LiteralArgumentBuilder<McmeCommandSender> SetCapture(){
        return HelpfulLiteralBuilder.literal("addCapture")
                .requires(Requirements::canEditCapture)
                .executes(EditExecutor::CreateCapture);
    }
    private LiteralArgumentBuilder<McmeCommandSender> DeleteCapture(){
        return HelpfulLiteralBuilder.literal("delCapture")
                .requires(Requirements::canEditCapture)
                .then(Arguments.CapturePointIndex()
                        .executes(EditExecutor::DelCapture));
    }
    private LiteralArgumentBuilder<McmeCommandSender> GetMapState(){
        return null; //TODO: Implement GetMapState command
    }
    private LiteralArgumentBuilder<McmeCommandSender> GetGamemodeState(){
        return null; //TODO: Implement GetGamemodeState command
    }

    /* private LiteralArgumentBuilder<McmeCommandSender> ???(){
        return
    }*/
    //</editor-fold>
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        McmeCommandSender wrappedSender = new PVPCommandSender(sender);
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
