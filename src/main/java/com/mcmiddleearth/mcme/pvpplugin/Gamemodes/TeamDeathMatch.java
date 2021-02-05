package com.mcmiddleearth.mcme.pvpplugin.Gamemodes;

import com.google.common.collect.Lists;
import com.mcmiddleearth.mcme.pvpplugin.Gamemodes.helpers.BaseGamemode;
import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mcmiddleearth.mcme.pvpplugin.PVP.Team;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.PVP.Matchmaker;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TeamDeathMatch extends BaseGamemode {
    //TODO: Check functionality and start bugfixing
    public enum gameState { IDLE, COUNTDOWN, RUNNING}
    @Getter
    private final List<String> neededPoints = new ArrayList<>(Lists.newArrayList("BlueSpawn", "RedSpawn"));
    private PVPPlugin pvpPlugin;
    private final Team blue = new Team("Blue", ChatColor.BLUE);
    private final Team red = new Team("Red", ChatColor.RED);
    private gameState gState;
    private Matchmaker matchmaker;

    @Override
    public void start(Map m, PVPPlugin plugin){
        matchmaker = new Matchmaker(plugin);
        ArrayList<Set<Player>> teams = matchmaker.makeTeams(2, super.getPlayers());
        blue.setMembers(teams.get(0));
        red.setMembers(teams.get(1));
        pvpPlugin = plugin;
        super.getImportantEvents().forEach(event -> pvpPlugin.addEventListener(event, this));
        gState = gameState.RUNNING;
        super.start(m, plugin);
    }

    @Override
    public void end(){
        blue.clear();
        red.clear();
        gState = gameState.IDLE;
        super.getImportantEvents().forEach(event -> pvpPlugin.removeEventListener(event, this));
        super.end();
    }

    @Override
    public void handleEvent(PlayerDeathEvent event) {
        if(blue.isInTeam(event.getEntity())) {
            blue.getDeadMembers().add(event.getEntity());
        } else {
            red.getDeadMembers().add(event.getEntity());
        }
        if(blue.allDead()) {
            super.setWinners(blue.getMembers());
            end();
        }
        if(red.allDead()) {
            super.setWinners(red.getMembers());
            end();
        }
        super.handleEvent(event);
    }

    @Override
    public void handleEvent(PlayerMoveEvent event) {
        if(gState == gameState.COUNTDOWN){
            event.setCancelled(true);
        } else {
            super.handleEvent(event);
        }
    }

    @Override
    public void handleEvent(Object event) {

    }

    public void respawnPlayer(Player player){

    }

    @Override
    public void onPlayerJoin(Player player) {
        if(super.getMaxPlayers() >= super.getPlayers().size()){
            player.sendMessage(ChatColor.YELLOW + "Can't join, the game is full.");
        } else{
            if (!super.getPlayers().contains(player)){
                super.getPlayers().add(player);
                if(gState!=gameState.IDLE){
                    matchmaker.addPlayer(Lists.newArrayList(blue, red), player).getMembers().add(player);
                    respawnPlayer(player);
                }
            }else{
                player.sendMessage(ChatColor.YELLOW + "You can't rejoin this game.");
            }
        }
    }

    @Override
    public void onPlayerLeave(Player player) {
        blue.getMembers().remove(player);
        red.getMembers().remove(player);
    }
}
