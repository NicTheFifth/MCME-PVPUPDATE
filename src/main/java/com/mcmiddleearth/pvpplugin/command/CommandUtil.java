package com.mcmiddleearth.pvpplugin.command;

import com.mcmiddleearth.command.sender.McmeCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Predicate;

public class CommandUtil {
    public static Player getPlayer(McmeCommandSender sender){
       return ((Player)((PVPCommandSender) sender).getSender());
    }

    @SafeVarargs
    public static Predicate<McmeCommandSender> multiRequirements(Predicate<McmeCommandSender>... predicates){
        return (sender -> Arrays.stream(predicates).map(predicate -> predicate.test(sender))
            .reduce(true, (x,y) -> x && y));
    }
}
