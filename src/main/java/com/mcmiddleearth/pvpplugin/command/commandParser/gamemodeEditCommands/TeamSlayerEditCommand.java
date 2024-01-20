package com.mcmiddleearth.pvpplugin.command.commandParser.gamemodeEditCommands;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.command.commandParser.Requirements;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.TeamSlayerEditor;

public class TeamSlayerEditCommand implements GamemodeEditCommand{
    public static void addToCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .then(HelpfulLiteralBuilder.literal("testTS")
                .requires(c -> Requirements.isGamemodeEditorOf(TeamSlayerEditor.class, c))
                .executes(c ->{
                    CommandUtil.getPlayer(c.getSource())
                        .sendMessage("TestTS gotten!");
                    return 1;
                }));
    }
}
