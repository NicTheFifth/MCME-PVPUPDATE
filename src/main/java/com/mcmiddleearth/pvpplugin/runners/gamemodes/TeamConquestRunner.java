package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONTeamConquest;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.ScoreGoal;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import com.mcmiddleearth.pvpplugin.util.Team;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class TeamConquestRunner extends GamemodeRunner implements ScoreGoal {

    TCTeam blueTeam = new TCTeam();
    TCTeam redTeam = new TCTeam();

    int scoreGoal;

    public static int DefaultScoreGoal(){
        return 20;
    }

    //0 neutral, -50 = blueTeam, 50 = redTeam
    HashMap<Location, Integer> capturePointsProgress = new HashMap<>();

    public TeamConquestRunner(JSONMap map, int scoreGoal){
        region = AreaTranscriber.TranscribeArea(map);
        this.scoreGoal = scoreGoal;
        JSONTeamConquest teamConquest = map.getJSONTeamConquest();
        teamConquest.getCapturePoints().stream().map(LocationTranscriber::TranscribeFromJSON).forEach(location ->
                this.capturePointsProgress.put(location, 0));
        maxPlayers = teamConquest.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new TCListener();
        initTeams(map);
        initPoints();
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
    }
    //<editor-fold defaultstate="collapsed" desc="Teams">
    private void initTeams(JSONMap map){
        initBlueTeam(map.getJSONTeamConquest());
        initRedTeam(map.getJSONTeamConquest());
        initSpectator(map.getSpawn());
    }


    private void initBlueTeam(JSONTeamConquest jsonTeamConquest){
        blueTeam.setPrefix("Blue");
        blueTeam.setTeamColour(Color.BLUE);
        blueTeam.setChatColor(BLUE);
        blueTeam.setGameMode(GameMode.ADVENTURE);
        blueTeam.setKit(createKit(Color.BLUE));
        blueTeam.setSpawnLocations(jsonTeamConquest.getBlueSpawns()
                .stream().map(LocationTranscriber::TranscribeFromJSON)
                .collect(Collectors.toList()));
    }

    private void initRedTeam(JSONTeamConquest jsonTeamConquest){
        redTeam.setPrefix("Red");
        redTeam.setChatColor(RED);
        redTeam.setTeamColour(Color.RED);
        redTeam.setGameMode(GameMode.ADVENTURE);
        redTeam.setKit(createKit(Color.RED));
        redTeam.setSpawnLocations(jsonTeamConquest.getRedSpawns()
                .stream().map(LocationTranscriber::TranscribeFromJSON)
                .collect(Collectors.toList()));
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
    //<editor-fold defaultstate="collapsed" desc="Capture points">
    private void initPoints(){
        capturePointsProgress.keySet().forEach(this::spawnCapturePoint);
    }

    private void spawnCapturePoint(Location capturePoint){
        capturePoint.getBlock().setType(Material.BEACON);

        capturePoint.getBlock().getRelative(0, -1, -1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(0, -1, 0).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(0, -1, 1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(1, -1, -1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(1, -1, 0).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(1, -1, 1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(-1, -1, -1).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(-1, -1, 0).setType(Material.IRON_BLOCK);
        capturePoint.getBlock().getRelative(-1, -1, 1).setType(Material.IRON_BLOCK);
    }

    private void deleteCapturePoint(Location capturePoint){
        capturePoint.getBlock().setType(Material.AIR);
        capturePoint.getBlock().getRelative(0, 1, 0).setType(Material.AIR);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start Conditions">
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
    //<editor-fold defaultstate="collapsed" desc="Start Actions">
    @Override
    protected void initStartActions() {
        startActions.add(() -> players.forEach(player -> JoinTeamConquest(player, true)));
        startActions.add(()-> ScoreboardEditor.InitTeamConquest(scoreboard,
                scoreGoal));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End Actions">
    @Override
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
            if(redTeam.getPoints() == scoreGoal)
                PVPPlugin.getInstance().sendMessage(mm.deserialize("<red>Red Won!!!</red>"));
            else
                PVPPlugin.getInstance().sendMessage(mm.deserialize("<blue>Blue Won!!!</blue>"));});
        endActions.get(false).add(() ->
                capturePointsProgress.keySet().forEach(this::deleteCapturePoint));
        endActions.get(true).add(() ->
                capturePointsProgress.keySet().forEach(this::deleteCapturePoint));
        endActions.get(false).add(() ->{
                    PlayerRespawnEvent.getHandlerList().unregister(eventListener);
                    PlayerInteractEvent.getHandlerList().unregister(eventListener);
        });
        endActions.get(true).add(()->{
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
        });
    }

    private Set<Player> getLosingTeamMembers() {
        if(redTeam.getPoints() == scoreGoal)
            return blueTeam.getMembers();
        return redTeam.getMembers();
    }

    private Set<Player> getWinningTeamMembers() {
        if(redTeam.getPoints() == scoreGoal)
            return redTeam.getMembers();
        return blueTeam.getMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="End Conditions">
    private boolean hasEmptyTeam(){
        return redTeam.getOnlineMembers().isEmpty() ||
                blueTeam.getOnlineMembers().isEmpty();
    }

    private boolean isScoreGoalReached(){
        return redTeam.getPoints() >= scoreGoal ||
                blueTeam.getPoints() >= scoreGoal;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Join">
    @Override
    protected void initJoinConditions() {
        joinConditions.put(((player) ->
                        redTeam.getPoints() <=(scoreGoal *0.9) ||
                                blueTeam.getPoints() <=(scoreGoal *0.9)),
                mm.deserialize("<aqua>The game is close to over, you cannot join.</aqua>"));
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinTeamConquest(player, false));
    }
    private void JoinTeamConquest(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            player.sendMessage(mm.deserialize("<aqua>You joined the game.</aqua>"));
            return;
        }
        if(redTeam.getMembers().contains(player)) {
            joinTeam(player, redTeam);
            return;
        }
        if(blueTeam.getMembers().contains(player)) {
            joinTeam(player, blueTeam);
            return;
        }
        TeamHandler.addToTeam((team -> team.getOnlineMembers().size()),
                Pair.of(redTeam, () -> joinTeam(player, redTeam)),
                Pair.of(blueTeam, () -> joinTeam(player, blueTeam)));
    }

    private void joinTeam(Player player, TCTeam team) {
        team.getOnlineMembers().add(player);
        Matchmaker.addMember(player, team);
        TeamHandler.spawn(player, team);
        PVPPlugin.getInstance().sendMessage(mm.deserialize("<color><name> has joined the <prefix></color>",
                Placeholder.styling("color", team.getChatColor()),
                Placeholder.parsed("name", player.getName()),
                Placeholder.parsed("prefix", team.getPrefix())));
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leave);
    }

    private void leave(Player player){
        if (redTeam.getMembers().contains(player)) {
            leaveTeam(player, redTeam);
        } else {
            leaveTeam(player, blueTeam);
        }
        if(hasEmptyTeam())
            end(true);
    }

    private void leaveTeam(Player player, Team team){
        team.getOnlineMembers().remove(player);
        PVPPlugin.getInstance().sendMessage(mm.deserialize("<color><name> has left the game.</color>",
                Placeholder.styling("color", team.getChatColor()),
                Placeholder.parsed("name", player.getName())));
    }
    //</editor-fold>

    @Override
    public TagResolver.Single getPlayerPrefix(Player player){
        if(!players.contains(player))
            return null;
        if(blueTeam.getMembers().contains(player))
            return Placeholder.parsed("prefix", blueTeam.getPrefix());
        if(redTeam.getMembers().contains(player))
            return Placeholder.parsed("prefix", redTeam.getPrefix());
        return null;
    }

    @Override
    public TagResolver.Single getPlayerColor(Player player){
        if(!players.contains(player))
            return null;
        if(blueTeam.getMembers().contains(player))
            return Placeholder.styling("color", blueTeam.getChatColor());
        if(redTeam.getMembers().contains(player))
            return Placeholder.styling("color", redTeam.getChatColor());
        return null;
    }

    @Override
    public String getGamemode() {
        return Gamemodes.TEAMCONQUEST;
    }

    @Override
    public int getScoreGoal() {
        return scoreGoal;
    }

    @Override
    public void setScoreGoal(int scoreGoal) {
        this.scoreGoal = scoreGoal;
    }

    private class TCListener extends GamemodeListener{
        public TCListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e ->{
                Player player = e.getEntity();
                int redPoints = redTeam.controlledPoints - blueTeam.controlledPoints;
                if(redTeam.getMembers().contains(player))
                    blueTeam.addPoints(Math.max(-redPoints, 0));
                else
                    redTeam.addPoints(Math.max(redPoints, 0));
                ScoreboardEditor.updateValueTeamConquest(scoreboard,redTeam,blueTeam);
                if(isScoreGoalReached())
                    end(false);
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(gameState != State.RUNNING)
                return;
            if(redTeam.getMembers().contains(player))
                TeamHandler.respawn(e, redTeam);
            if(blueTeam.getMembers().contains(player))
                TeamHandler.respawn(e, blueTeam);
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent e) {
            Player p = e.getPlayer();
            if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                return;
            if(gameState != State.RUNNING )
                return;
            if(!players.contains(p))
                return;
            if(e.getClickedBlock() == null)
                return;
            if(!e.getClickedBlock().getType().equals(Material.BEACON))
                return;
            e.setUseInteractedBlock(Event.Result.DENY);
            Location capturePoint = e.getClickedBlock().getLocation();
            Location keyPoint = capturePoint.clone();
            int oldPointValue = capturePointsProgress.get(capturePoint);
            int newPointValue= oldPointValue;
            if(redTeam.getMembers().contains(p))
                newPointValue += 1;
            else
                newPointValue -= 1;
            if(Math.abs(newPointValue) > 50)
                return;
            switch(newPointValue){
                case 0:
                    if(capturePoint.add(0,1,0).getBlock().getType() != Material.AIR) {
                        if (oldPointValue < 0)
                            blueTeam.removeControlledPoint();
                        else
                            redTeam.removeControlledPoint();
                    }
                    capturePoint.getBlock().setType(Material.AIR);
                    p.sendMessage(mm.deserialize("<color>Point is neutralised!!</color>",
                            Placeholder.styling("color",
                                    (newPointValue<oldPointValue)? blueTeam.getChatColor() : redTeam.getChatColor())));
                    break;
                case 50:
                    redTeam.addControlledPoint();
                    capturePoint.add(0,1,0).getBlock().setType(Material.RED_STAINED_GLASS);
                    PVPPlugin.getInstance().sendMessage(mm.deserialize("<color>Red team has captured a point</color>",
                                    Placeholder.styling("color", redTeam.getChatColor())));
                    break;
                case -50:
                    blueTeam.addControlledPoint();
                    capturePoint.add(0,1,0).getBlock().setType(Material.BLUE_STAINED_GLASS);
                    PVPPlugin.getInstance().sendMessage(
                            String.format("<%s>Blue team has captured a point</%s>",
                                    blueTeam.getChatColor(),
                                    blueTeam.getChatColor()));
                    break;
            }
            p.sendMessage(mm.deserialize("<gray>Point is now at <value>!</gray>",
                    Placeholder.parsed("value", String.valueOf(newPointValue))));
            capturePointsProgress.put(keyPoint, newPointValue);
        }

        @EventHandler
        public void onPlayerDamage(EntityDamageByEntityEvent e){
            if(!(e.getEntity() instanceof Player player))
                return;
            Player damager = null;
            if(e.getDamager() instanceof Player hitter)
                damager = hitter;
            if(e.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter)
                damager = shooter;
            if(damager == null)
                return;
            if((redTeam.getMembers().contains(player) && redTeam.getMembers().contains(damager)) ||
                    (blueTeam.getMembers().contains(player) && blueTeam.getMembers().contains(damager)))
                e.setCancelled(true);
        }
    }

    public static class TCTeam extends Team{
        private int points = 0;

        private int controlledPoints = 0;

        public void addPoints(int pointsToAdd) {
            points += pointsToAdd;
        }

        public int getPoints(){
            return points;
        }

        public void addControlledPoint(){
            controlledPoints+=1;
        }

        public void removeControlledPoint(){
            controlledPoints -=1;
        }
    }
}
