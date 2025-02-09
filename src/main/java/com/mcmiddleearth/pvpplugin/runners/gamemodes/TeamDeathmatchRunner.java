package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamDeathMatch;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import com.mcmiddleearth.pvpplugin.util.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class TeamDeathmatchRunner extends GamemodeRunner {
    TDMTeam redTeam = new TDMTeam();
    TDMTeam blueTeam = new TDMTeam();
    public TeamDeathmatchRunner(JSONMap map){
        region = AreaTranscriber.TranscribeArea(map);
        JSONTeamDeathMatch teamDeathMatch = map.getJSONTeamDeathMatch();
        maxPlayers = teamDeathMatch.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new TeamDeathmatchRunner.TDMListener();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    private void initTeams(JSONMap map){
        initTeamBlue(map.getJSONTeamDeathMatch().getBlueSpawn());
        initTeamRed(map.getJSONTeamDeathMatch().getRedSpawn());
        initSpectator(map.getSpawn());
    }

    private void initTeamBlue(JSONLocation blueSpawn){
        blueTeam.setPrefix("Blue");
        blueTeam.setTeamColour(Color.BLUE);
        blueTeam.setChatColor(BLUE);
        blueTeam.setKit(createKit(Color.BLUE));
        blueTeam.setSpawnLocations(
            List.of(LocationTranscriber.TranscribeFromJSON(blueSpawn)));
        blueTeam.setGameMode(GameMode.ADVENTURE);
    }

    private void initTeamRed(JSONLocation redSpawn){
        redTeam.setPrefix("Red");
        redTeam.setTeamColour(Color.RED);
        redTeam.setChatColor(RED);
        redTeam.setKit(createKit(Color.RED));
        redTeam.setSpawnLocations(
            List.of(LocationTranscriber.TranscribeFromJSON(redSpawn)));
        redTeam.setGameMode(GameMode.ADVENTURE);
    }

    private @NotNull Kit createKit(Color color){
        Consumer<Player> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.INFINITY, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                color));
            returnInventory.forEach(KitEditor::setUnbreaking);
        });
        return new Kit(invFunc);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start conditions"
    @Override
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
                redTeam.getOnlineMembers().size() + blueTeam.getOnlineMembers().size();
        startConditions.put(() -> totalInTeams.get() != players.size() || !redTeam.getOnlineMembers().isEmpty(),
                mm.deserialize("<red>Can't start, red team has to have at least one online player.</red>"));
        startConditions.put(() -> totalInTeams.get() != players.size() ||!blueTeam.getOnlineMembers().isEmpty(),
                mm.deserialize("<red>Can't start, blue team has to have at least one online player.</red>"));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start actions"
    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> JoinTeamDeathmatch(player, true)));
        startActions.add(()-> ScoreboardEditor.InitTeamDeathmatch(scoreboard,
            redTeam, blueTeam
            ));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End actions">
    protected void initEndActions() {
        endActions.get(false).add(() ->
            getWinningTeamMembers().forEach(player -> {
                PlayerStatEditor.addWon(player);
                PlayerStatEditor.addPlayed(player);
            }));
        endActions.get(false).add(() ->
            getLosingTeamMembers().forEach(player -> {
                PlayerStatEditor.addLost(player);
                PlayerStatEditor.addPlayed(player);
            }));
        endActions.get(false).add(() ->{
            if(redTeam.hasAliveMembers())
                PVPPlugin.getInstance().sendMessage(mm.deserialize("<red>Red won!!!</red>"));
            else
                PVPPlugin.getInstance().sendMessage(mm.deserialize("<blue>Blue won!!!</blue>"));});
        endActions.get(false).add(() ->
            PlayerRespawnEvent.getHandlerList().unregister(eventListener));
        endActions.get(true).add(()->
            PlayerRespawnEvent.getHandlerList().unregister(eventListener));
    }

    private Set<Player> getLosingTeamMembers() {
        if(redTeam.hasAliveMembers())
            return blueTeam.getMembers();
        return redTeam.getMembers();
    }

    private Set<Player> getWinningTeamMembers() {
        if(redTeam.hasAliveMembers())
            return redTeam.getMembers();
        return blueTeam.getMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Join">
    @Override
    protected void initJoinConditions() {
        joinConditions.put(((player) ->
                redTeam.AliveMembers() >= 3 && blueTeam.AliveMembers() >= 3),
            mm.deserialize("<red>The game is close to over, you cannot join.</red>"));
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinTeamDeathmatch(player, false));
    }

    private void JoinTeamDeathmatch(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            player.sendMessage(mm.deserialize("<aqua>You joined the game.</aqua>"));
            return;
        }
        if(redTeam.getMembers().contains(player)) {
            join(player, redTeam);
            return;
        }
        if(blueTeam.getMembers().contains(player)) {
            join(player, blueTeam);
            return;
        }
        TeamHandler.addToTeam((team -> team.getOnlineMembers().size()),
            Pair.of(redTeam, () -> join(player, redTeam)),
            Pair.of(blueTeam, () -> join(player, blueTeam)));
    }
    private void join(Player player, TDMTeam team){
        team.getOnlineMembers().add(player);
        Matchmaker.addMember(player, team);
        if(team.getDeadMembers().contains(player)){
            TeamHandler.spawn(player, spectator);
            PVPPlugin.getInstance().sendMessageTo(
                    String.format("<aqua>You've joined the %s team, but were already dead.</aqua>",
                            team.getPrefix()), player);
            return;
        }
        TeamHandler.spawn(player, team);
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has joined the %s team!</%s>",
                        team.getChatColor(),
                        player.getName(),
                        team.getPrefix(),
                        team.getChatColor()));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leaveTeamDeathmatch);
    }

    private void leaveTeamDeathmatch(Player player){
        if (redTeam.getMembers().contains(player)) {
            leave(player,redTeam);
        } else {
            leave(player,blueTeam);
        }
        if(!blueTeam.hasAliveMembers() || !redTeam.hasAliveMembers()) {
            end(true);
        }
        ScoreboardEditor.updateValueTeamDeathmatch(scoreboard, redTeam,blueTeam);
    }

    private void leave(Player player, Team team){
        team.getOnlineMembers().remove(player);
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has left the game</%s>",
                        team.getChatColor(),
                        player.getName(),
                        team.getChatColor()));
    }
    //</editor-fold>

    @Override
    public @NotNull Boolean trySendSpectatorMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        return trySendMessage(player, messageBuilder);
    }

    public Boolean trySendMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        if(!players.contains(player))
            return false;
        String prefix = null;
        List<TagResolver> resolvers = new ArrayList<>();
        if(blueTeam.getDeadMembers().contains(player))
            prefix = "Dead Blue";
        if(redTeam.getDeadMembers().contains(player))
            prefix = "Dead Red";
        if(spectator.getMembers().contains(player))
            prefix = "Spectator";

        if(prefix != null){
            resolvers.add(Placeholder.parsed("prefix", prefix));
            resolvers.add(Placeholder.styling("color", spectator.getChatColor()));
            Set<Player> deads = new HashSet<>(blueTeam.getDeadMembers());
            deads.addAll(spectator.getMembers());
            deads.addAll(redTeam.getDeadMembers());
            PVPPlugin.getInstance().sendMessageTo(messageBuilder.apply(resolvers),deads);
            return true;
        }
        Team team = null;
        if(redTeam.getMembers().contains(player))
            team = redTeam;
        if(blueTeam.getMembers().contains(player))
            team=blueTeam;
        if(team == null)
            return false;

        resolvers.add(Placeholder.parsed("prefix", team.getPrefix()));
        resolvers.add(Placeholder.styling("color", team.getChatColor()));
        PVPPlugin.getInstance().sendMessage(messageBuilder.apply(resolvers));
        return true;
    }

    @Override
    public String getGamemode() {
        return Gamemodes.TEAMDEATHMATCH;
    }

    private class TDMListener extends GamemodeListener{
        public TDMListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e ->{
                Player player = e.getEntity();
                if(redTeam.getMembers().contains(player))
                    redTeam.deadMembers.add(player);
                else
                    blueTeam.deadMembers.add(player);
                player.setGameMode(spectator.getGameMode());
                ScoreboardEditor.updateValueTeamDeathmatch(scoreboard,redTeam,blueTeam);
                if(!redTeam.hasAliveMembers() || !blueTeam.hasAliveMembers())
                    end(false);
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            if(gameState != State.RUNNING)
                return;
            TeamHandler.respawn(e, spectator);
        }

        @EventHandler
        public void onPlayerDamage(EntityDamageByEntityEvent e){
            if(!(e.getEntity() instanceof Player player))
                return;
            if(!(e.getDamager() instanceof Player damager))
                return;
            if((redTeam.getMembers().contains(player) && redTeam.getMembers().contains(damager)) ||
                    (blueTeam.getMembers().contains(player) && blueTeam.getMembers().contains(damager)))
                e.setCancelled(true);
        }
    }
    public static class TDMTeam extends Team {
        Set<Player> deadMembers = new HashSet<>();

        public boolean hasAliveMembers(){
            return onlineMembers.containsAll(deadMembers);
        }
        public Set<Player> getDeadMembers() {return  deadMembers;}

        public Integer AliveMembers(){
            Set<Player> aliveMembers = new HashSet<>(onlineMembers);
            aliveMembers.removeAll(deadMembers);
            return aliveMembers.size();
        }
    }
}