package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONOneInTheQuiver;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OneInTheQuiverRunner extends GamemodeRunner {

    private final int scoreGoal;
    public static int defaultScoreGoal = 20;

    private final List<Location> spawns;
    private final Map<UUID, PlayerTeam> OITQplayers = new HashMap<>();

    public OneInTheQuiverRunner(JSONMap map, int scoreGoal){
        region = AreaTranscriber.TranscribeArea(map);
        JSONOneInTheQuiver oneInTheQuiver = map.getJSONOneInTheQuiver();
        this.scoreGoal = scoreGoal;
        maxPlayers = oneInTheQuiver.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new OITQListener();
        this.spawns = oneInTheQuiver.getSpawns().stream().map(LocationTranscriber::TranscribeFromJSON).collect(Collectors.toList());
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();

    }

    @Override
    protected void initStartConditions() {

    }

    @Override
    protected void initStartActions() {

    }

    @Override
    protected void initEndActions() {

    }

    @Override
    protected void initJoinConditions() {

    }

    @Override
    protected void initJoinActions() {

    }

    @Override
    protected void initLeaveActions() {

    }

    @Override
    public String getGamemode() {
        return Gamemodes.ONEINTHEQUIVER;
    }

    public class OITQListener extends GamemodeListener{
        public OITQListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                if(!players.contains(player))
                    return;
                Player killer = player.getKiller();
                if(killer == null)
                    return;
                killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                OITQplayers.get(killer.getUniqueId()).addKill();
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(!players.contains(player))
                return;
            TeamHandler.respawn(e, spawns);
        }

        @EventHandler
        public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
            if(!(e.getEntity() instanceof Player))
                return;
            Player player = (Player) e.getEntity();
            if(!(e.getDamageSource().getDirectEntity() instanceof Arrow))
                return;

        }
    }

    private class PlayerTeam {
         int kills = 0;
         Color chatColor;

         public void addKill(){
             kills++;
             if(kills >= scoreGoal)
                 end(false);
         }
    }
}
