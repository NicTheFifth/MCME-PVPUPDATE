/*
 * This file is part of MCME-pvp.
 *
 * MCME-pvp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MCME-pvp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MCME-pvp.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package com.mcmiddleearth.mcme.pvpplugin.command.handler;

import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import com.mcmiddleearth.mcme.pvpplugin.command.argument.NewMapArgumentType;
import com.mcmiddleearth.mcme.pvpplugin.command.argument.ExistingMapArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mcmiddleearth.mcme.pvpplugin.command.argument.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class PVPCommandHandler {
    public class PVPCommand extends CommandDispatcher<Player>{

        private final com.mcmiddleearth.mcme.pvpplugin.PVPPlugin PVPPlugin;
        private volatile HashMap<String, Map> maps;
        @Getter
        private volatile Set<String> mapNames;
        @Getter
        private volatile boolean locked = true;
        @Getter
        private String Message = "PvP-server Locked";
        private Queue<Map> gameQueue = new LinkedList<>();
        private Queue<Integer> parameterQueue = new LinkedList<>();;
        @Getter
        private Map nextGame = null;
        private int parameter;
        @Getter @Setter
        private Map runningGame = null;

        public PVPCommand(com.mcmiddleearth.mcme.pvpplugin.PVPPlugin PVPPlugin1) {
            PVPPlugin = PVPPlugin1;
            register(LiteralArgumentBuilder.<Player>literal("pvp")
                    .then(LiteralArgumentBuilder.<Player>literal("map")
                            .then(LiteralArgumentBuilder.<Player>literal("list").executes(c -> {
                                mapList(c.getSource());
                                return 1;} ))
                            .then(RequiredArgumentBuilder.<Player, String>argument("name", new ExistingMapArgumentType()).requires(c -> c.hasPermission(Permission.PVP_ADMIN)).executes(c ->{
                                createMap(c.getArgument("name", String.class), c.getSource());
                                return 1; }))
                            .then(LiteralArgumentBuilder.<Player>literal("spawn").requires(c -> c.hasPermission(Permission.PVP_JOIN))
                                    .then(RequiredArgumentBuilder.<Player, String>argument("name", new ExistingMapArgumentType()).executes(c ->{
                                        teleport(c.getArgument("name", String.class), c.getSource());
                                        return 1;}))))
                    .then(LiteralArgumentBuilder.<Player>literal("game")
                            .then(LiteralArgumentBuilder.<Player>literal("quickstart").requires( c -> c.hasPermission(Permission.PVP_RUN))
                                    .then(RequiredArgumentBuilder.<Player, String>argument( "map", new ExistingMapArgumentType()).executes(c -> {
                                        createGame(c.getArgument("map", String.class), c.getSource());
                                        return 1;} )
                                            .then(RequiredArgumentBuilder.<Player,String>argument("variable", new IntArgumentType()).executes(c -> {
                                                createVarGame(c.getArgument("map", String.class), c.getArgument("variable", String.class), c.getSource());
                                                return 1;} )
                                                    .then(LiteralArgumentBuilder.<Player>literal("test").executes(c -> {
                                                        createVarTest(c.getArgument("map", String.class), c.getArgument("variable", String.class), c.getSource());
                                                        return 1;} )))
                                            .then(LiteralArgumentBuilder.<Player>literal("test").executes(c -> {
                                                createTest(c.getArgument("map", String.class), c.getSource());
                                                return 1;} ))))
                            .then(LiteralArgumentBuilder.<Player>literal("start").requires( c -> c.hasPermission(Permission.PVP_RUN)).executes(c -> {
                                startGame(c.getSource());
                                return 1;} ))
                            .then(LiteralArgumentBuilder.<Player>literal("end").requires( c -> c.hasPermission(Permission.PVP_RUN)).executes(c -> {
                                endGame(c.getSource());
                                return 1;} ))
                            .then(LiteralArgumentBuilder.<Player>literal("getgames").executes(c -> {
                                getGames(c.getSource());
                                return 1;} )))
                    .then(LiteralArgumentBuilder.<Player>literal("join").executes(c -> {
                        join(c.getSource());
                        return 1;} ))
                    .then(LiteralArgumentBuilder.<Player>literal("rules")
                            .then(RequiredArgumentBuilder.<Player, String>argument("gamemode", new CommandStringArgument("infected", "teamslayer", "teamdeathmatch", "ringbearer", "oneinthequiver", "teamconquest", "deathrun","capturetheflag")).executes(c -> {
                                rules(c.getArgument("gamemode", String.class), c.getSource());
                                return 1;} )))
                    .then(LiteralArgumentBuilder.<Player>literal("pipe").executes(c -> {
                        pipe(c.getSource());
                        return 1;} ))
                    .then(LiteralArgumentBuilder.<Player>literal("stats").executes(c -> {
                        stats(c.getSource());
                        return 1;} )
                            .then(LiteralArgumentBuilder.<Player>literal("clear").requires( c -> c.hasPermission(Permission.PVP_ADMIN)).executes(c -> {
                                statsClear(c.getSource());
                                return 1;} )).executes(c -> {
                                stats(c.getSource());
                                return 1;}))
                    .then(LiteralArgumentBuilder.<Player>literal("lobby").executes(c -> {
                        lobby(c.getSource());
                        return 1;} ))
                    .then(LiteralArgumentBuilder.<Player>literal("broadcast").requires( c -> c.hasPermission(Permission.PVP_RUN)).executes(c->{
                        broadcast(c.getSource());
                        return 1;
                    }))
            );

            register(LiteralArgumentBuilder.<Player>literal("mapeditor").requires( c -> c.hasPermission(Permission.PVP_ADMIN))
                    .then(RequiredArgumentBuilder.<Player, String>argument("map", new NewMapArgumentType())
                            .then(LiteralArgumentBuilder.<Player>literal("name")
                                    .then(RequiredArgumentBuilder.<Player, String>argument("name", new NewMapArgumentType()).executes(c -> {
                                        mapEditorName(c.getArgument("map", String.class), c.getArgument("name", String.class), c.getSource());
                                        return 1;
                                    })))
                            .then(LiteralArgumentBuilder.<Player>literal("title")
                                    .then(RequiredArgumentBuilder.<Player, String>argument("title", new CommandStringVariableArgument()).executes(c -> {
                                        mapEditorTitle(c.getArgument("map", String.class), c.getArgument("title", String.class), c.getSource());
                                        return 1;
                                    })))
                            .then(LiteralArgumentBuilder.<Player>literal("gm")
                                    .then(RequiredArgumentBuilder.<Player, String>argument("gm", new CommandStringArgument("FreeForAll", "Infected", "OneInTheQuiver", "Ringbearer", "TeamConquest", "TeamDeathmatch", "TeamSlayer", "DeathRun","CaptureTheFlag")).executes(c -> {
                                        mapEditorGm(c.getArgument("map", String.class), c.getArgument("gm", String.class), c.getSource());
                                        return 1;
                                    })))
                            .then(LiteralArgumentBuilder.<Player>literal("max")
                                    .then(RequiredArgumentBuilder.<Player, String>argument("max", new IntArgumentType()).executes(c -> {
                                        mapEditorMax(c.getArgument("map", String.class), c.getArgument("max", String.class), c.getSource());
                                        return 1;
                                    })))
                            .then(LiteralArgumentBuilder.<Player>literal("rp")
                                    .then(RequiredArgumentBuilder.<Player, String>argument("rp", new CommandStringArgument("eriador", "rohan", "lothlorien", "gondor", "moria", "mordor")).executes(c -> {
                                        mapEditorRp(c.getArgument("map", String.class), c.getArgument("rp", String.class), c.getSource());
                                        return 1;
                                    })))
                            .then(LiteralArgumentBuilder.<Player>literal("setarea").executes( c -> {
                                setArea(c.getArgument("map", String.class), c.getSource());
                                return 1;
                            }))
                            .then(LiteralArgumentBuilder.<Player>literal("delete").executes( c -> {
                                deleteMap(c.getArgument("map", String.class), c.getSource());
                                return 1;
                            }))
                            .then(LiteralArgumentBuilder.<Player>literal("spawn")
                                    .then(RequiredArgumentBuilder.<Player, String>argument( "spawn", new CommandStringVariableArgument())
                                            .then(LiteralArgumentBuilder.<Player>literal("delete").executes(c ->{
                                                deleteSpawn(c.getArgument("map", String.class),c.getArgument("spawn", String.class), c.getSource());
                                                return 1;
                                            }))
                                            .then(LiteralArgumentBuilder.<Player>literal("create").executes( c -> {
                                                createSpawn(c.getArgument("map", String.class), c.getArgument("spawn", String.class), c.getSource());
                                                return 1;
                                            }))
                                            .then(LiteralArgumentBuilder.<Player>literal("setloc").executes( c -> {
                                                setSpawnLoc(c.getArgument("map", String.class), c.getArgument("spawn", String.class), c.getSource());
                                                return 1;
                                            })))
                                    .then(LiteralArgumentBuilder.<Player>literal("show").executes( c -> {
                                        spawnShow(c.getArgument("map", String.class), c.getSource());
                                        return 1;
                                    }))
                                    .then(LiteralArgumentBuilder.<Player>literal("hide").executes( c -> {
                                        spawnHide(c.getArgument("map", String.class), c.getSource());
                                        return 1;
                                    }))
                            )
                            .then(LiteralArgumentBuilder.<Player>literal("listspawns").executes( c -> {
                                listSpawns(c.getArgument("map", String.class), c.getSource());
                                return 1;
                            }))
                    )
            );
        }
    //TODO: Make PVPCommand handler
}

    private void listSpawns(String map, Player source) {
        MapEditor.sendSpawnMessage(argument, source);
    }

    private void spawnHide(String map, Player source) {
        MapEditor.HideSpawns(source);
    }

    private void spawnShow(String map, Player source) {
        MapEditor.HideSpawns(source);
        MapEditor.ShowSpawns(argument, source);
    }

    private void setSpawnLoc(String map, String spawn, Player source) {
        MapEditor.PointLocEdit(argument1, argument2, source);
    }

    private void createSpawn(String map, String spawn, Player source) {
        MapEditor.PointCreate(argument1, argument2, source);
    }

    private void deleteSpawn(String map, String spawn, Player source) {
        MapEditor.PointDelete(argument1, argument2, source);
    }

    private void deleteMap(String map, Player source) {
    }

    private void setArea(String map, Player source) {
    }

    private void mapEditorRp(String map, String rp, Player source) {
        MapEditor.MapRPSet(argument1,argument2,source);
    }

    private void mapEditorMax(String map, String max, Player source) {
        MapEditor.MapMaxSet(argument1, argument2, source);
    }

    private void mapEditorGm(String map, String gm, Player source) {
        MapEditor.MapGamemodeSet(argument1, argument2, source);
    }

    private void mapEditorTitle(String map, String title, Player source) {
        MapEditor.MapTitleEdit(argument1, argument2, source);
    }

    private void mapEditorName(String map, String name, Player source) {
        MapEditor.MapNameEdit(argument1, argument2, source);
    }

    private void broadcast(Player source) {
        if(nextGame != null)
            sendBroadcast(source, nextGame);
        else
            source.sendMessage("Can't send broadcast, next game is null");
    }

    private void lobby(Player source) {
        source.sendMessage(ChatColor.GREEN + "Sending Signs");
        for(Map map : Map.maps.values()){
            ItemStack sign = new ItemStack(Material.OAK_WALL_SIGN);
            ItemMeta im = sign.getItemMeta();
            im.setDisplayName(Map.getName());
            String gamemode = "none";
            if(map.getGm() != null){
                gamemode = map.getGmType();
            }
            im.setLore(Arrays.asList(new String[] {map.getTitle(),  gamemode,  String.valueOf(map.getMax())}));
            sign.setItemMeta(im);
            source.getInventory().addItem(sign);
        }
    }

    private void statsClear(Player source) {
        for(File f : new File(PVPPlugin.getStatDirectory() + PVPPlugin.getFileSep()).listFiles()) {
            f.delete();
        }
        for(PlayerStat pS : PlayerStat.getPlayerStats().values()) {
            pS.setKills(0);
            pS.setDeaths(0);
            pS.setGamesLost(0);
            pS.setGamesWon(0);
            pS.setGamesSpectated(0);
            pS.setGamesPlayed(0);
            pS.getPlayersKilled().clear();
        }
    }

    private void stats(Player source) {
        PlayerStat ps = PlayerStat.getPlayerStats().get(source.getName());

        source.sendMessage(ChatColor.GREEN + "Showing stats for " + source.getDisplayName());
        source.sendMessage(ChatColor.GRAY + "Kills: " + ps.getKills());
        source.sendMessage(ChatColor.GRAY + "Deaths: " + ps.getDeaths());
        source.sendMessage(ChatColor.GRAY + "Games Played: " + ps.getGamesPlayed());
        source.sendMessage(ChatColor.GRAY + "    Won: " + ps.getGamesWon());
        source.sendMessage(ChatColor.GRAY + "    Lost: " + ps.getGamesLost());
        source.sendMessage(ChatColor.GRAY + "Games Spectated: " + ps.getGamesSpectated());
    }

    private void pipe(Player source) {
        GearHandler.giveCustomItem(source, PIPE);
    }

    private void rules(String gamemode, Player source) {
        switch(argument) {
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
                source.sendMessage(ChatColor.GRAY + "Capture the enemy flag and escort it to your base while protecting your own");
        }
    }

    private void join(Player source) {
        Map m;

        if(nextGame != null){
            m = nextGame;
        }
        else if(runningGame != null){
            m = runningGame;
        }
        else{
            source.sendMessage(ChatColor.RED + "There is no queued or running game!");
        }
    }

    private void getGames(Player source) {
        if(runningGame != null)
            source.sendMessage(ChatColor.BLUE + "Now playing: " + runningGame.getGmType() + " on " + runningGame.getTitle());
        if(nextGame != null)
            source.sendMessage(ChatColor.BLUE + "Next game: " + nextGame.getGmType() + " on " + nextGame.getTitle());
        if(!gameQueue.isEmpty())
            source.sendMessage(ChatColor.BLUE + "Queued game: " + gameQueue.peek().getGmType() + " on " + gameQueue.peek().getTitle());
    }

    private void endGame(Player source) {
        if(nextGame != null){
            nextGame.getGm().getPlayers().clear();
            nextGame = null;
            for(Player pl : Bukkit.getOnlinePlayers()){
                ChatHandler.getPlayerColors().put(pl.getName(), ChatColor.WHITE);
                pl.setPlayerListName(ChatColor.WHITE + pl.getName());
                pl.setDisplayName(ChatColor.WHITE + pl.getName());
                BukkitTeamHandler.removeFromBukkitTeam(pl);
                pl.sendMessage(ChatColor.GRAY + "The queued game was canceled! You'll need to rejoin when another game is queued.");
            }
            ChatHandler.getPlayerPrefixes().clear();
            if(!gameQueue.isEmpty() && !parameterQueue.isEmpty()) {
                nextGame = gameQueue.poll();
                parameter = parameterQueue.poll();
                source.sendMessage("Map: " + nextGame.getTitle() + ", Gamemode: " + nextGame.getGmType() + ", Parameter: "+ parameter + "\nIf you wish to announce the game type /pvp broadcast!");
            }
        } else if(runningGame != null){

            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.sendMessage(ChatColor.GRAY + runningGame.getGmType() + " on " + runningGame.getTitle() + " was ended by a staff!");
            }
            runningGame.getGm().End(runningGame);
        }
        else  {
            source.sendMessage(ChatColor.GRAY + "There is no game to end!");
        }
    }

    private void startGame(Player source) {
        if(nextGame == null){
            source.sendMessage(ChatColor.RED + "Can't start! No game is queued!");
        } else if(nextGame.getGm().getPlayers().size() == 0 ){
            source.sendMessage(ChatColor.RED + "Can't start! No players have joined!");
        } else if(runningGame == null){
            nextGame.getGm().Start(nextGame, parameter);
            runningGame = nextGame;
            nextGame = null;
        }
        else{
            source.sendMessage(ChatColor.RED + "Can't start! There's already a game running!");
        }
        MapEditor.HideSpawns(source);
    }

    private void createTest(String map, Player source) {
        Map m = Map.maps.get(argument);
        if(m.getGm().requiresParameter().equals("none"))
        {
            if(nextGame == null & runningGame == null) {
                source.sendMessage("Map: " + m.getTitle() + ", Gamemode: " + m.getGmType());
                parameter = 0;
                nextGame = m;
            }else{
                source.sendMessage("Map: " + m.getTitle() + ", Gamemode: " + m.getGmType() + " is queued!");
                gameQueue.add(m);
                parameterQueue.add(0);
            }
        }
        else{
            source.sendMessage(m.getTitle() + " " + m.getGmType() + " requires a variable!");
        }
    }

    private void createVarTest(String map, String variable, Player source) {
        Map m = Map.maps.get(argument1);
        if(m.getGm().requiresParameter().equals("none"))
        {
            createTest(argument1, source);
        }
        else{
            if(nextGame == null && runningGame == null) {
                source.sendMessage("Map: " + m.getTitle() + ", Gamemode: " + m.getGmType() + ", Parameter: "+ argument2);
                parameter = Integer.parseInt(argument2);
                nextGame = m;
            }else{
                source.sendMessage("Map: " + m.getTitle() + ", Gamemode: " + m.getGmType() + ", Parameter: "+ argument2 + " is queued!");
                gameQueue.add(m);
                parameterQueue.add(Integer.parseInt(argument2));
            }
        }
    }

    private void createVarGame(String map, String variable, Player source) {
        Map n = Map.maps.get(argument1);
        if(n.getGm().requiresParameter().equals("none"))
        {
            createGame(argument1, source);
        }
        else{
            if(nextGame == null && runningGame == null) {
                source.sendMessage("Map: " + n.getTitle() + ", Gamemode: " + n.getGmType() + ", Parameter: "+ argument2);
                sendBroadcast(source,n);
                parameter = Integer.parseInt(argument2);
                nextGame = n;
            }else{
                source.sendMessage("Map: " + n.getTitle() + ", Gamemode: " + n.getGmType() + ", Parameter: "+ argument2 + " is queued!");
                gameQueue.add(n);
                parameterQueue.add(Integer.parseInt(argument2));
            }
        }
    }

    private void createGame(String map, Player source) {
        Map n = Map.maps.get(argument);
        if(n.getGm().requiresParameter().equals("none"))
        {
            if(nextGame==null && runningGame==null) {
                source.sendMessage("Map: " + n.getTitle() + ", Gamemode: " + n.getGmType());
                sendBroadcast(source,n);
                parameter = 0;
                nextGame = n;
            }else{
                source.sendMessage("Map: " + n.getTitle() + ", Gamemode: " + n.getGmType() + " is queued!");
                gameQueue.add(n);
                parameterQueue.add(0);
            }
        }
        else{
            source.sendMessage(ChatColor.RED + n.getTitle() + " " + n.getGmType() + " requires a variable.");
        }
    }

    private void teleport(String name, Player source) {
        Location spawn = Map.maps.get(argument).getSpawn().toBukkitLoc();
        source.teleport(spawn);
        source.sendMessage("You have been teleported to " + argument);
    }

    private void createMap(String name, Player source) {
        MapEditor.MapCreator(argument, source);
    }

    private void mapList(Player source) {
        for(String m: mapNames)
            source.sendMessage(ChatColor.GREEN + maps.get(m).getName() + ChatColor.WHITE + " | " + ChatColor.BLUE + maps.get(m).getTitle());
    }

}
