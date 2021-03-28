package com.mcmiddleearth.mcme.pvpplugin.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVP.Matchmaker;
import com.mcmiddleearth.mcme.pvpplugin.PVP.Team;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.exception.GameModeNotSupportedException;
import com.mcmiddleearth.mcme.pvpplugin.json.JSONLocation;
import com.mcmiddleearth.mcme.pvpplugin.json.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.gamemodes.JSONTeamDeathMatch;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class GamemodeTeamDeathMatch extends BaseGamemodeRunner {

    private Team teamBlue = new Team("Blue", ChatColor.BLUE);
    private Team teamRed = new Team("Red", ChatColor.RED);
    private final Matchmaker matchmaker = new Matchmaker(getPlugin());

    public GamemodeTeamDeathMatch(JSONMap map, PVPPlugin plugin) {
        super(map, plugin);
        JSONTeamDeathMatch teamDeathMatch = map.getTeamDeathMatch();

        if (teamDeathMatch == null) {
            throw new GameModeNotSupportedException(map.getTitle(), GamemodeTeamDeathMatch.class.getName());
        }

        Set<Location> bukkitBlueSpawnLocations = teamDeathMatch.getBlueSpawn().stream().map(JSONLocation::toBukkitLoc).collect(Collectors.toSet());
        Set<Location> bukkitRedSpawnLocations = teamDeathMatch.getRedSpawn().stream().map(JSONLocation::toBukkitLoc).collect(Collectors.toSet());

        teamBlue.setSpawnLocations(bukkitBlueSpawnLocations);
        teamRed.setSpawnLocations(bukkitRedSpawnLocations);

    }

    @Override
    protected void prepareStart() {
        matchmaker.makeTeams(getPlayers(), teamBlue, teamRed);
    }

    @Override
    protected void start() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected boolean handlePlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
        matchmaker.addPlayer(Matchmaker.ADDITION_TYPE.SPAWN, playerJoinEvent.getPlayer(), teamBlue, teamRed);
        return false;
    }
}
