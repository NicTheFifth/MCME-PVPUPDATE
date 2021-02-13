package com.mcmiddleearth.mcme.pvpplugin.gamemodes;

import com.mcmiddleearth.mcme.pvpplugin.PVP.Matchmaker;
import com.mcmiddleearth.mcme.pvpplugin.PVP.Team;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.exception.GameModeNotSupportedException;
import com.mcmiddleearth.mcme.pvpplugin.json.JSONMap;
import com.mcmiddleearth.mcme.pvpplugin.json.gamemodes.JSONTeamDeathMatch;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Set;

public class GamemodeTeamDeathMatch extends BaseGamemodeRunner {

    private Team teamBlue;
    private Team teamRed;

    public GamemodeTeamDeathMatch(JSONMap map, PVPPlugin plugin) {
        super(map, plugin);
        JSONTeamDeathMatch teamDeathMatch = map.getTeamDeathMatch();

        if (teamDeathMatch == null) {
            throw new GameModeNotSupportedException(map.getTitle(), GamemodeTeamDeathMatch.class.getName());
        }

    }

    private void makeTeams() {
        Matchmaker matchmaker = new Matchmaker(getPlugin());
        ArrayList<Set<Player>> teams = matchmaker.makeTeams(2, getPlayers());
        teamBlue.setMembers(teams.get(0));
        teamRed.setMembers(teams.get(1));
    }

    @Override
    protected void prepareStart() {
        this.makeTeams();
    }

    @Override
    protected void start() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected boolean handlePlayerMoveEvent(PlayerMoveEvent playerMoveEvent) {
        return true;
    }
}
