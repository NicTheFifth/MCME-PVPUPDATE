package com.mcmiddleearth.mcme.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameExecutor {
    public static int GetRule(CommandContext<McmeCommandSender> c){
        Logger.getLogger("PVPPlugin").log(Level.INFO, c.getArgument("gamemode", String.class));
        if(c.getSource() instanceof Player) {
            Player source = (Player) c.getSource();
            switch (c.getArgument("gamemode", String.class)) {
                case "freeforall":
                    source.sendMessage(ChatColor.GREEN + "Free For All Rules");
                    source.sendMessage(ChatColor.GRAY + "Every man for himself, madly killing everyone! Highest number of kills wins.");
                    break;
                case "infected":
                    source.sendMessage(ChatColor.GREEN + "Infected Rules");
                    source.sendMessage(ChatColor.GRAY + "Everyone starts as a Survivor, except one person, who is Infected. Infected gets a Speed effect, but has less armor");
                    source.sendMessage(ChatColor.GRAY + "If a Survivor is killed, they become Infected. Infected players have infinite respawns");
                    source.sendMessage(ChatColor.GRAY + "If all Survivors are infected, Infected team wins. If the time runs out with Survivors remaining, Survivors win.");
                    break;
                case "oneinthequiver":
                    source.sendMessage(ChatColor.GREEN + "One in the Quiver Rules");
                    source.sendMessage(ChatColor.GRAY + "Everyone gets an axe, a bow, and one arrow, which kills in 1 shot if the bow is fully drawn.");
                    source.sendMessage(ChatColor.GRAY + "Every man is fighting for himself. If they get a kill or die, they get another arrow, up to a max of 5 arrows");
                    source.sendMessage(ChatColor.GRAY + "First to 21 kills wins.");
                    break;
                case "ringbearer":
                    source.sendMessage(ChatColor.GREEN + "Ringbearer Rules");
                    source.sendMessage(ChatColor.GRAY + "Two teams, each with a ringbearer, who gets The One Ring (which of course gives invisibility)");
                    source.sendMessage(ChatColor.GRAY + "As long as the ringbearer is alive, the team can respawn.");
                    source.sendMessage(ChatColor.GRAY + "Once the ringbearer dies, that team cannot respawn. The first team to run out of members loses.");
                    break;
                case "teamconquest":
                    source.sendMessage(ChatColor.GREEN + "Team Conquest Rules");
                    source.sendMessage(ChatColor.GRAY + "Two teams. There are 3 beacons, which each team can capture by repeatedly right clicking the beacon.");
                    source.sendMessage(ChatColor.GRAY + "Points are awarded on kills, based on the difference between each team's number of beacons.");
                    source.sendMessage(ChatColor.GRAY + "i.e. if Red has 3 beacons and Blue has 0, Red gets 3 point per kill. If Red has 1 and Blue has 2, Red doesn't get points for a kill.");
                    source.sendMessage(ChatColor.GRAY + "First team to a certain point total wins.");
                    break;
                case "teamdeathmatch":
                    source.sendMessage(ChatColor.GREEN + "Team Deathmatch Rules");
                    source.sendMessage(ChatColor.GRAY + "Two teams, and no respawns. First team to run out of players loses.");
                    break;
                case "teamslayer":
                    source.sendMessage(ChatColor.GREEN + "Team Slayer Rules");
                    source.sendMessage(ChatColor.GRAY + "Two teams, and infinite respawns. 1 point per kill. First team to a certain point total wins.");
                    break;
                case "deathrun":
                    source.sendMessage(ChatColor.GREEN + "Death Run Rules");
                    source.sendMessage(ChatColor.GRAY + "One death, and lots of runners. Runners have to reach the end goal before the time limit or getting killed by death.");
                case "capturetheflag":
                    source.sendMessage(ChatColor.GREEN + "Capture the Flag Rules");
                    source.sendMessage(ChatColor.GRAY + "Capture the enemy flag(banner), by right clicking it, and escort it to your base beacon while protecting your own. To capture the enemy flag, right click on it, it will be placed on your head, then with it on your head right click your spawn, and you score 1 point.");
            }
            return 1;
        }
        return 0;
    }
}
