package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONInfected;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.TimeLimit;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.KitEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ScoreboardEditor;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.TeamHandler;
import com.mcmiddleearth.pvpplugin.statics.Gamemodes;
import com.mcmiddleearth.pvpplugin.util.Kit;
import com.mcmiddleearth.pvpplugin.util.Matchmaker;
import com.mcmiddleearth.pvpplugin.util.PlayerStatEditor;
import com.mcmiddleearth.pvpplugin.util.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.kyori.adventure.text.format.NamedTextColor.BLUE;

public class InfectedRunner extends GamemodeRunner implements TimeLimit {
    int timeLimit;

    public static int DefaultTimeLimit(){
        return 300;
    }

    Team survivors = new Team();
    Team infected = new Team();

    public InfectedRunner(JSONMap map, int timeLimit){
        region = AreaTranscriber.TranscribeArea(map);
        JSONInfected infected = map.getJSONInfected();
        this.timeLimit = timeLimit;
        maxPlayers = infected.getMaximumPlayers();
        mapName = map.getTitle();
        eventListener = new IListener();
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
        initSpectator(map.getSpawn());
        initSurvivor(map.getJSONInfected().getSurvivorSpawn());
        initInfected(map.getJSONInfected().getInfectedSpawn());
    }

    private void initSurvivor(JSONLocation survivorSpawn){
        survivors.setPrefix("Survivor");
        survivors.setTeamColour(Color.BLUE);
        survivors.setChatColor(BLUE);
        survivors.setGameMode(GameMode.ADVENTURE);
        survivors.setKit(createSurvivorKit());
        survivors.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(survivorSpawn)));
    }

    private Kit createSurvivorKit(){
        Consumer<Player> invFunc = (player -> {
            createInfectedKit(Color.BLUE).accept(player);
            PlayerInventory returnInventory = player.getInventory();
            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                    Color.BLUE));
            returnInventory.forEach(KitEditor::setUnbreaking);
        });
        return new Kit(invFunc);

    }

    private void initInfected(JSONLocation infectedSpawn){
        infected.setPrefix("Infected");
        infected.setTeamColour(Color.RED);
        infected.setChatColor(NamedTextColor.RED);
        infected.setGameMode(GameMode.ADVENTURE);
        //TODO: Fix the infected kit to be slightly stronger
        infected.setKit(new Kit(createInfectedKit(Color.RED)));
        infected.setSpawnLocations(List.of(LocationTranscriber.TranscribeFromJSON(infectedSpawn)));
    }

    private @NotNull Consumer<Player> createInfectedKit(Color color){
        return (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.INFINITY, 1);
            returnInventory.setItem(1, bow);
            returnInventory.setItem(2, new ItemStack(Material.ARROW));
            returnInventory.forEach(item -> KitEditor.setItemColour(item,
                    color));
            returnInventory.forEach(KitEditor::setUnbreaking);
        });
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start Conditions">
    @Override
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
                infected.getOnlineMembers().size() + survivors.getOnlineMembers().size();
        startConditions.put(() -> totalInTeams.get() != players.size() || !infected.getOnlineMembers().isEmpty(),
                mm.deserialize("<red>Can't start, infected has to have at least one online player.</red>"));
        startConditions.put(() -> totalInTeams.get() != players.size() ||!survivors.getOnlineMembers().isEmpty(),
                mm.deserialize("<red>Can't start, survivors has to have at least one online player.</red>"));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Start Actions">
    @Override
    protected void initStartActions() {
        startActions.add(() -> {
            initWithRandomInfected();
            players.forEach(player -> JoinInfected(player, true));
        });
        startActions.add(()-> ScoreboardEditor.InitInfected(scoreboard, timeLimit, infected, survivors));
        startActions.add(() -> new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState == State.ENDED){
                    this.cancel();
                    return;
                }
                if(gameState == State.COUNTDOWN)
                {
                    return;
                }
                if (timeLimit == 0) {
                    end(false);
                    gameState = State.ENDED;
                    this.cancel();
                    return;
                }
                timeLimit--;
                ScoreboardEditor.UpdateTimeInfected(scoreboard, timeLimit);
            }
        }.runTaskTimer(PVPPlugin.getInstance(),100,20));
    }

    private void initWithRandomInfected() {
        if (!infected.getOnlineMembers().isEmpty())
            return;
        int randomInfectedIndex = ThreadLocalRandom.current().nextInt(0, players.size());
        infected.getMembers().add((Player) players.toArray()[randomInfectedIndex]);
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
        endActions.get(false).add(() -> {
                    if (survivors.getOnlineMembers().isEmpty())
                        PVPPlugin.getInstance().sendMessage(mm.deserialize("<red>Infected Won!!!</red>"));
                    else
                        PVPPlugin.getInstance().sendMessage(mm.deserialize("<blue>Survivors Won!!!</blue>"));
                });
        endActions.get(true).add(()-> PlayerRespawnEvent.getHandlerList().unregister(eventListener));
        endActions.get(false).add(() -> players.forEach(player -> player.removePotionEffect(PotionEffectType.SPEED)));
        endActions.get(true).add(() -> players.forEach(player -> player.removePotionEffect(PotionEffectType.SPEED)));
    }

    private Set<Player> getLosingTeamMembers() {
        if(survivors.getOnlineMembers().isEmpty())
            return survivors.getMembers();
        return infected.getMembers();
    }

    private Set<Player> getWinningTeamMembers() {
        if(survivors.getOnlineMembers().isEmpty())
            return infected.getMembers();
        return survivors.getMembers();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Join">
    @Override
    protected void initJoinConditions() {
        joinConditions.put((player ->
                        timeLimit <=60),
                mm.deserialize("<aqua>The game is close to over, you cannot join.</aqua>"));
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinInfected(player, false));
    }

    private void JoinInfected(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            player.sendMessage(mm.deserialize("<aqua>You joined the game.</aqua>"));
            return;
        }
        if(infected.getMembers().contains(player)) {
            join(player, infected);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1));
            return;
        }
        if(survivors.getMembers().contains(player)) {
            join(player, survivors);
            return;
        }
        TeamHandler.addToTeamInfected(
                Pair.of(infected, () ->{
                    join(player, infected);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2));}),
                Pair.of(survivors, () -> join(player, survivors)));
    }

    private void join(Player player, Team team){
        team.getOnlineMembers().add(player);
        Matchmaker.addMember(player, team);
        TeamHandler.spawn(player, team);
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has joined the %s</%s>",
                        team.getChatColor(),
                        player.getName(),
                        team.getPrefix(),
                        team.getChatColor()));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Leave">
    @Override
    protected void initLeaveActions() {
        leaveActions.add(this::leave);
    }

    private void leave(Player player){
        if (infected.getMembers().contains(player)) {
            leave(player, infected);
        } else {
            leave(player, survivors);
        }
        if(infected.getOnlineMembers().isEmpty())
            end(true);
        if(survivors.getOnlineMembers().isEmpty())
            end(false);
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

    public Boolean trySendMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        if(!players.contains(player))
            return false;
        Team team = null;
        if(infected.getMembers().contains(player))
            team = infected;
        if(survivors.getMembers().contains(player))
            team=survivors;
        if(team == null)
            return false;

        PVPPlugin.getInstance().sendMessage(messageBuilder.apply(
                List.of(Placeholder.parsed("prefix", team.getPrefix()),
                        Placeholder.styling("color", team.getChatColor()))));
        return true;
    }

    @Override
    public int getTimeLimit() {
        return timeLimit;
    }

    @Override
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public String getGamemode() {
        return Gamemodes.INFECTED;
    }

    private class IListener extends GamemodeListener{
        public IListener(){
            initOnPlayerDeathActions();
        }
        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e -> {
                Player player = e.getEntity();
                if(survivors.getOnlineMembers().remove(player)) {
                    survivors.getMembers().remove(player);
                    Matchmaker.addMember(player, infected);
                    if(survivors.getOnlineMembers().isEmpty())
                        end(false);
                    ScoreboardEditor.UpdateTeamsInfected(scoreboard, infected, survivors);
                }
            });
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(gameState != State.RUNNING)
                return;
            if(infected.getMembers().contains(player)) {
                TeamHandler.respawn(e, infected);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1));
            }
        }

        @EventHandler
        public void onPlayerDamage(EntityDamageByEntityEvent e){
            if(!(e.getEntity() instanceof Player player))
                return;
            if(!(e.getDamager() instanceof Player damager))
                return;
            if((infected.getMembers().contains(player) && infected.getMembers().contains(damager)) ||
                    (survivors.getMembers().contains(player) && survivors.getMembers().contains(damager)))
                e.setCancelled(true);
        }
    }
}
