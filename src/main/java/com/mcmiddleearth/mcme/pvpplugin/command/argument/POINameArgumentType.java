package com.mcmiddleearth.mcme.pvpplugin.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class POINameArgumentType implements ArgumentType<String>,HelpfulArgumentType {
    @Override
    public void setTooltip(String tooltip) {

    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }
}
