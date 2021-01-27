package com.mcmiddleearth.mcme.pvpplugin.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;


public class IntArgumentType implements ArgumentType<String>,HelpfulArgumentType{

    private String tooltip;

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String o = reader.readString();
        try {
            if (Integer.parseInt(o) > 0)
                return o;
            else
                throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(tooltip)), new LiteralMessage(tooltip + o));
        } catch (Exception e) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(tooltip)), new LiteralMessage(tooltip + o));
        }
    }

    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

}