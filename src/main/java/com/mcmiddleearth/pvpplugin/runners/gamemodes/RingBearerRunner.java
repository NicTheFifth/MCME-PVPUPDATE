package com.mcmiddleearth.pvpplugin.runners.gamemodes;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONLocation;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.JSONRingBearer;
import com.mcmiddleearth.pvpplugin.json.transcribers.AreaTranscriber;
import com.mcmiddleearth.pvpplugin.json.transcribers.LocationTranscriber;
import com.mcmiddleearth.pvpplugin.runners.gamemodes.abstractions.GamemodeRunner;
import com.mcmiddleearth.pvpplugin.runners.runnerUtil.ChatUtils;
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
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class RingBearerRunner extends GamemodeRunner {

    RBTeam redTeam = new RBTeam();
    RBTeam blueTeam = new RBTeam();
    int timeSecBetweenRingUp = 16;

    public RingBearerRunner(JSONMap map){
        region = AreaTranscriber.TranscribeArea(map);
        JSONRingBearer ringBearer = map.getJSONRingBearer();
        eventListener= new RBListener();
        maxPlayers = ringBearer.getMaximumPlayers();
        mapName = map.getTitle();
        initTeams(map);
        initStartConditions();
        initStartActions();
        initEndActions();
        initJoinConditions();
        initJoinActions();
        initLeaveActions();
        ChatUtils.AnnounceNewGame("Ring Bearer", mapName, String.valueOf(maxPlayers));
    }

    private void initTeams(JSONMap map) {
        initRed(map.getJSONRingBearer().getRedSpawns());
        initBlue(map.getJSONRingBearer().getBlueSpawns());
        initSpectator(map.getSpawn());
    }

    private void initBlue(List<JSONLocation> blueSpawns) {
        blueTeam.setPrefix("Blue");
        blueTeam.setTeamColour(Color.BLUE);
        blueTeam.setChatColor(BLUE);
        blueTeam.setKit(createKit(Color.BLUE, false));
        blueTeam.setRingBearerKit(createKit(Color.BLUE, true));
        blueTeam.setSpawnLocations(
                blueSpawns.stream().map(LocationTranscriber::TranscribeFromJSON).collect(Collectors.toList()));
        blueTeam.setGameMode(GameMode.SURVIVAL);
    }

    private void initRed(List<JSONLocation> redSpawns) {
        redTeam.setPrefix("Red");
        redTeam.setTeamColour(Color.RED);
        redTeam.setChatColor(RED);
        redTeam.setKit(createKit(Color.RED, false));
        redTeam.setRingBearerKit(createKit(Color.RED, true));
        redTeam.setSpawnLocations(
                redSpawns.stream().map(LocationTranscriber::TranscribeFromJSON).collect(Collectors.toList()));
        redTeam.setGameMode(GameMode.SURVIVAL);
    }

    private Kit createKit(Color color, boolean isRingbearer) {
        Consumer<Player> invFunc = (x -> {
            PlayerInventory returnInventory = x.getInventory();
            returnInventory.clear();
            if(isRingbearer) {
                returnInventory.setHelmet(new ItemStack(Material.GLOWSTONE));
                returnInventory.setItem(3, new ItemStack(Material.GOLD_NUGGET));
            }
            else
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

    @Override
    protected void initStartConditions() {
        Supplier<Integer> totalInTeams = () ->
                redTeam.getOnlineMembers().size() + blueTeam.getOnlineMembers().size();
        startConditions.put(() ->
                        totalInTeams.get() != players.size() || !redTeam.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, red team has to have at least " +
                        "one online player.")
                        .color(Style.ERROR).create());
        startConditions.put(() ->
                        totalInTeams.get() != players.size() ||!blueTeam.getOnlineMembers().isEmpty(),
                new ComponentBuilder("Can't start, blue team has to have at least" +
                        " one online player.")
                        .color(Style.ERROR).create());
    }

    @Override
    protected void initStartActions() {
        startActions.add(() -> {
            players.forEach(player -> JoinRingBearer(player, true));
            if(redTeam.getRingBearer() == null)
                TeamHandler.SetRingBearer(redTeam);
            if(blueTeam.getRingBearer() == null)
                TeamHandler.SetRingBearer(blueTeam);
            redTeam.getOnlineMembers().forEach(player -> {
                if (redTeam.getRingBearer() == player)
                    sendBaseComponent(new ComponentBuilder("You're the ringbearer, survive as long as possible!").create(), player);
                else
                    sendBaseComponent(new ComponentBuilder(redTeam.getRingBearer().getName() + " is your ringbearer, kill the enemy ringbearer and team to win!").create(), player);
            });
            blueTeam.getOnlineMembers().forEach(player -> {
                if (blueTeam.getRingBearer() == player)
                    sendBaseComponent(new ComponentBuilder("You're the ringbearer, survive as long as possible!").create(), player);
                else
                    sendBaseComponent(new ComponentBuilder(blueTeam.getRingBearer().getName() + " is your ringbearer, kill the enemy ringbearer and team to win!").create(), player);
            });
        });
        startActions.add(()-> ScoreboardEditor.InitRingBearer(scoreboard, redTeam, blueTeam));
        startActions.add(() -> new BukkitRunnable() {
            @Override
            public void run() {
                if(gameState == State.ENDED) {
                    this.cancel();
                    return;
                }
                Player redBearer = redTeam.getRingBearer();
                Player blueBearer = blueTeam.getRingBearer();
                if(redBearer != null &&
                   !redBearer.hasPotionEffect(PotionEffectType.INVISIBILITY) &&
                   !redBearer.getInventory().contains(Material.GOLD_NUGGET, 10))
                    redBearer.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET));
                if(blueBearer != null &&
                   !blueBearer.hasPotionEffect(PotionEffectType.INVISIBILITY) &&
                   !blueBearer.getInventory().contains(Material.GOLD_NUGGET, 10))
                    blueBearer.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET));
            }
        }.runTaskTimer(PVPPlugin.getInstance(), 20L *(countDownTimer + timeSecBetweenRingUp), 20L * timeSecBetweenRingUp));
    }

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
            if(redTeam.hasAliveMembers()) {
                players.forEach(player ->
                        sendBaseComponent(
                                new ComponentBuilder("Red Won!!!").color(ChatColor.RED)
                                        .create(), player));
                spectator.getMembers().forEach(player ->
                        sendBaseComponent(
                                new ComponentBuilder("Red Won!!!").color(ChatColor.RED)
                                        .create(), player));
            }
            else{
                players.forEach(player ->
                        sendBaseComponent(
                                new ComponentBuilder("Blue Won!!!").color(ChatColor.BLUE)
                                        .create(), player));
                spectator.getMembers().forEach(player ->
                        sendBaseComponent(
                                new ComponentBuilder("Blue Won!!!").color(ChatColor.BLUE)
                                        .create(), player));}});
        endActions.get(false).add(() -> {
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
            EntityPotionEffectEvent.getHandlerList().unregister(eventListener);
        });
        endActions.get(true).add(()-> {
            PlayerRespawnEvent.getHandlerList().unregister(eventListener);
            PlayerInteractEvent.getHandlerList().unregister(eventListener);
            EntityPotionEffectEvent.getHandlerList().unregister(eventListener);
        });
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

    @Override
    protected void initJoinConditions() {
        joinConditions.put(((player) ->
                        gameState == State.QUEUED || (redTeam.AliveMembers() >= 3 && blueTeam.AliveMembers() >= 3)),
                new ComponentBuilder("The game is close to over, you cannot join.")
                        .color(Style.INFO)
                        .create());
    }

    @Override
    protected void initJoinActions() {
        joinActions.add(player -> JoinRingBearer(player, false));
    }

    private void JoinRingBearer(Player player, boolean onStart){
        if(!onStart && gameState == State.QUEUED) {
            sendBaseComponent(
                    new ComponentBuilder("You joined the game.").color(Style.INFO).create(),
                    player);
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
                Pair.of(redTeam, () -> join(player,redTeam)),
                Pair.of(blueTeam, () -> join(player,blueTeam)));
    }

    private void join(Player player, RBTeam team){
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
        if(!blueTeam.hasAliveMembers() || !redTeam.hasAliveMembers()) {
            end(true);
        }
        ScoreboardEditor.UpdateRingBearer(scoreboard, redTeam, blueTeam);

    }

    private void leaveTeam(Player player, RBTeam team){
        team.getOnlineMembers().remove(player);
        if(team.getRingBearer() == player) {
            PVPPlugin.getInstance().sendMessageTo(
                    String.format("<%s>Your ringbearer has left, a new one will be chosen in 5 seconds!</%s>",
                            team.getChatColor(),
                            team.getChatColor()),
                    team.getOnlineMembers());
            new BukkitRunnable() {
                @Override
                public void run() {
                    TeamHandler.SetRingBearer(team);
                    PVPPlugin.getInstance().sendMessageTo(
                            String.format("<%s>You're the ringbearer, survive as long as possible!</%s>",
                                    team.getChatColor(),
                                    team.getChatColor()),
                            team.getRingBearer());
                    Set<Player> playersMinusRB = new HashSet<>(team.getOnlineMembers());
                    playersMinusRB.remove(team.getRingBearer());
                    PVPPlugin.getInstance().sendMessageTo(
                            String.format("<%s>%s is your ringbearer, kill the enemy ringbearer and team to win!</%s>",
                                    team.getChatColor(),
                                    team.getRingBearer().getName(),
                                    team.getChatColor()),
                            playersMinusRB);
                }
            }.runTaskLater(PVPPlugin.getInstance(), 1000);
        }
        PVPPlugin.getInstance().sendMessage(
                String.format("<%s>%s has left the game.</%s>",
                        team.getChatColor(),
                        player.getName(),
                        team.getChatColor()));
    }

    @Override
    public Boolean trySendSpectatorMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        return trySendMessage(player, messageBuilder);
    }

    public Boolean trySendMessage(Player player, Function<List<TagResolver>, Component> messageBuilder){
        if(!players.contains(player))
            return false;
        String prefix = null;
        if(blueTeam.getDeadMembers().contains(player))
            prefix = "Dead Blue";
        if(redTeam.getDeadMembers().contains(player))
            prefix = "Dead Red";
        if(spectator.getMembers().contains(player))
            prefix = "Spectator";

        if(prefix != null){
            Set<Player> deads = new HashSet<>(blueTeam.getDeadMembers());
            deads.addAll(spectator.getMembers());
            deads.addAll(redTeam.getDeadMembers());
            PVPPlugin.getInstance().sendMessageTo(messageBuilder.apply(
                    List.of(Placeholder.parsed("prefix", prefix),
                            Placeholder.styling("color", spectator.getChatColor()))),
                    deads);
            return true;
        }
        Team team = null;
        if(redTeam.getMembers().contains(player))
            team = redTeam;
        if(blueTeam.getMembers().contains(player))
            team=blueTeam;
        if(team == null)
            return false;

        PVPPlugin.getInstance().sendMessage(messageBuilder.apply(
                List.of(Placeholder.parsed("prefix", team.getPrefix()),
                        Placeholder.styling("color", team.getChatColor()))));
        return true;
    }

    @Override
    public String getGamemode() {
        return Gamemodes.RINGBEARER;
    }
    private class RBListener extends GamemodeListener{

        public RBListener(){
            initOnPlayerDeathActions();
        }

        @Override
        protected void initOnPlayerDeathActions() {
            onPlayerDeathActions.add(e ->{
                Player player = e.getEntity();
                handleDeath(player, redTeam);
                handleDeath(player, blueTeam);
                ScoreboardEditor.UpdateRingBearer(scoreboard, redTeam, blueTeam);
                if(blueTeam.hasAliveMembers() && redTeam.hasAliveMembers())
                    return;
                end(false);
            });
        }

        private void handleDeath(Player player, RBTeam team){
            if(team.getMembers().contains(player)){
                if(team.isRingBearerDead()) {
                    team.getDeadMembers().add(player);
                }
                if(team.getRingBearer() == player){
                    team.killRingbearer();
                    PVPPlugin.getInstance().sendMessage(
                            String.format("<%s>%s team's ringbearer has been slain!</%s>",
                                    team.getChatColor(),
                                    team.getPrefix(),
                                    team.getChatColor())
                    );
                }
            }
        }

        @EventHandler
        public void onPlayerRespawn(PlayerRespawnEvent e){
            Player player = e.getPlayer();
            if(!players.contains(player))
                return;
            if(blueTeam.getMembers().contains(player)){
                if(blueTeam.getDeadMembers().contains(player)){
                    player.setGameMode(spectator.getGameMode());
                    TeamHandler.respawn(e, spectator);
                    return;
                }
                blueTeam.getKit().getInventory().accept(player);
                TeamHandler.respawn(e, blueTeam);
                return;
            }
            if(redTeam.getDeadMembers().contains(player)){
                player.setGameMode(spectator.getGameMode());
                TeamHandler.respawn(e, spectator);
                return;
            }
            redTeam.getKit().getInventory().accept(player);
            TeamHandler.respawn(e, redTeam);
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent e){
            Player player = e.getPlayer();
            if(!players.contains(player))
                return;
            if(!player.getInventory().getItemInMainHand().getType().equals(Material.GOLD_NUGGET))
                return;
            if(player.getInventory().contains(Material.GOLD_NUGGET, 10)){
                player.getInventory().remove(Material.GOLD_NUGGET);
                player.addPotionEffect(
                        new PotionEffect(PotionEffectType.BLINDNESS, 200, 0, true, false));
                player.addPotionEffect(
                        new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0, true, false));
                ItemStack empty = new ItemStack(Material.AIR);
                player.getInventory().setItemInOffHand(empty);
                player.getInventory().setHelmet(empty);
                player.getInventory().setChestplate(empty);
                player.getInventory().setLeggings(empty);
                player.getInventory().setBoots(empty);
                player.getInventory().setItemInOffHand(empty);
                sendBaseComponent(new ComponentBuilder("You've turned invisible, keep your hands empty and run!").create(), player);
            }
        }

        @EventHandler
        public void onEntityPotionEffect(EntityPotionEffectEvent e){
            if(!(e.getEntity() instanceof Player player))
                return;
            if(e.getCause() != EntityPotionEffectEvent.Cause.EXPIRATION && e.getModifiedType() != PotionEffectType.INVISIBILITY)
                return;
            if(redTeam.getRingBearer() == player)
                redTeam.getRingBearerKit().getInventory().accept(player);
            if(blueTeam.getRingBearer() == player)
                blueTeam.getRingBearerKit().getInventory().accept(player);
            sendBaseComponent(new ComponentBuilder("You've turned visible again!").create(), player);
        }
    }

    public static class RBTeam extends Team {
        Player ringBearer;
        Kit ringBearerKit;
        Set<Player> deadMembers = new HashSet<>();

        public void killRingbearer(){
            ringBearer = null;
        }

        public boolean isRingBearerDead(){return ringBearer == null;}

        public Integer AliveMembers(){
            Set<Player> aliveMembers = new HashSet<>(onlineMembers);
            aliveMembers.removeAll(deadMembers);
            return aliveMembers.size();
        }
        public boolean hasAliveMembers(){
            return AliveMembers() != 0;
        }

        public Set<Player> getDeadMembers(){return deadMembers;}

        public Kit getRingBearerKit(){return ringBearerKit;}

        public void setRingBearerKit(Kit ringBearerKit){
            this.ringBearerKit=ringBearerKit;
        }
        public Player getRingBearer(){return ringBearer;}
        public void setRingBearer(Player player){ringBearer = player;}
    }
}
