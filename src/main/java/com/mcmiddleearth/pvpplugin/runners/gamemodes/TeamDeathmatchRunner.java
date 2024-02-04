package com.mcmiddleearth.pvpplugin.runners.gamemodes;

public class TeamDeathmatchRunner /*extends GamemodeRunner*/ {
    //TODO: Edit Later
//    TDMTeam red = new TDMTeam();
//    TDMTeam blue = new TDMTeam();
//    Team spectator = new Team();
//    Scoreboard scoreboard;
//
//    //<editor-fold defaultstate="collapsed" desc="Initialisation">
//    public TeamDeathmatchRunner(JSONMap map, Boolean isPrivate) throws Exception {
//        //TODO: add a check if this game can actually be run
//        JSONTeamDeathMatch tdm = map.getJSONTeamDeathMatch();
//        createTeams(tdm);
//        createSpectator(map.getSpawn());
//        region = AreaTranscriber.TranscribeArea(map);
//        this.maxPlayers = tdm.getMaximumPlayers();
//        this.isPrivate = isPrivate;
//        gameState = State.QUEUED;
//    }
//
//    //<editor-fold defaultstate="collapsed" desc="Teams">
//    private void createTeams(JSONTeamDeathMatch gamemode) {
//        createRed(gamemode.getRedSpawn());
//        createBlue(gamemode.getBlueSpawn());
//    }
//
//    private void createRed(JSONLocation spawn) {
//        red.setPrefix("Red");
//        red.setTeamColour(Color.RED);
//        red.setKit(redKit());
//        red.setSpawnLocations(new ArrayList<>(List.of(
//                LocationTranscriber.TranscribeFromJSON(spawn)
//        )));
//        red.setGameMode(GameMode.ADVENTURE);
//    }
//
//    @Contract(value = " -> new", pure = true)
//    private @NotNull Kit redKit() {
//        Function<Player, Void> invFunc = (x -> {
//            PlayerInventory returnInventory = x.getInventory();
//            returnInventory.clear();
//            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
//            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
//            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
//            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
//            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
//            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
//            ItemStack bow = new ItemStack(Material.BOW);
//            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
//            returnInventory.setItem(1, bow);
//            returnInventory.setItem(2, new ItemStack(Material.ARROW));
//            returnInventory.forEach(item -> KitEditor.setItemColour(item, red.getTeamColour()));
//            return null;
//        });
//        return new Kit(invFunc);
//    }
//
//    private void createBlue(JSONLocation spawn) {
//        blue.setPrefix("Blue");
//        blue.setTeamColour(Color.BLUE);
//        blue.setKit(blueKit());
//        blue.setSpawnLocations(new ArrayList<>(List.of(
//                LocationTranscriber.TranscribeFromJSON(spawn)
//        )));
//        blue.setGameMode(GameMode.ADVENTURE);
//    }
//
//    @Contract(value = " -> new", pure = true)
//    private @NotNull Kit blueKit() {
//        Function<Player, Void> invFunc = (x -> {
//            PlayerInventory returnInventory = x.getInventory();
//            returnInventory.clear();
//            returnInventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
//            returnInventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
//            returnInventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
//            returnInventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
//            returnInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
//            returnInventory.setItem(0, new ItemStack(Material.IRON_SWORD));
//            ItemStack bow = new ItemStack(Material.BOW);
//            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
//            returnInventory.setItem(1, bow);
//            returnInventory.setItem(2, new ItemStack(Material.ARROW));
//            returnInventory.forEach(item -> KitEditor.setItemColour(item, blue.getTeamColour()));
//            return null;
//        });
//        return new Kit(invFunc);
//    }
//
//    private void createSpectator(JSONLocation spawn) {
//        spectator.setPrefix("Spectator");
//        spectator.setTeamColour(Color.SILVER);
//        spectator.setSpawnLocations(new ArrayList<>(List.of(
//                LocationTranscriber.TranscribeFromJSON(spawn)
//        )));
//        spectator.setGameMode(GameMode.SPECTATOR);
//    }
//    //</editor-fold>
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="Start">
//
//    public void start() {
//        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
//        //TODO: balance matchmaking
//        AtomicBoolean joinRed = new AtomicBoolean(false);
//        players.forEach(player -> {
//            if(joinRed.get())
//                Matchmaker.addMember(player, red);
//            else
//                Matchmaker.addMember(player, blue);
//            joinRed.set(!joinRed.get());
//        });
//        PVPPlugin.getInstance().getServer().getOnlinePlayers().forEach(player ->{
//            if(!blue.getMembers().contains(player) && !red.getMembers().contains(player))
//                spectator.getMembers().add(player);
//        });
//        createScoreboard();
//        players.forEach(player -> player.setScoreboard(scoreboard));
//        TeamHandler.spawnAll(red,blue,spectator);
//        pvpPlugin.getPluginManager().registerEvents(new Listeners(), pvpPlugin);
//        countDown();
//    }
//    private void countDown() {
//        gameState = State.COUNTDOWN;
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                if (countDownTimer == 0) {
//                    players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts!"));
//                    gameState = State.RUNNING;
//                    this.cancel();
//                    return;
//                }
//                players.forEach(player -> player.sendMessage(ChatColor.GREEN + "Game starts in " + countDownTimer));
//                countDownTimer--;
//            }
//        }.runTaskTimer(PVPPlugin.getInstance(),0,20);
//    }
//    private void createScoreboard(){
//        Objective Points = scoreboard.registerNewObjective("TDM Scoreboard", "dummy", "Remaining:");
//        Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.getAlive());
//        Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.getAlive());
//        Points.setDisplaySlot(DisplaySlot.SIDEBAR);
//    }
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="End">
//    @Override
//    public void end(boolean stopped) {
//        PVPPlugin pvpPlugin = PVPPlugin.getInstance();
//        if(!stopped){
//            getWinningTeamMembers().forEach(player->{
//                PlayerStatEditor.addWon(player);
//                PlayerStatEditor.addPlayed(player);
//            });
//            getLosingTeamMembers().forEach(player->{
//                PlayerStatEditor.addLost(player);
//                PlayerStatEditor.addPlayed(player);
//            });
//        }
//        players.forEach(player -> {
//            player.getInventory().clear();
//            player.getActivePotionEffects().clear();
//            player.setGameMode(GameMode.ADVENTURE);
//            player.teleport(pvpPlugin.getSpawn());
//        });
//        if (!stopped) spectator.getMembers().forEach(PlayerStatEditor::addSpectate);
//        gameState = State.ENDED;
//        pvpPlugin.setActiveGame(null);
//    }
//
//    private Set<Player> getWinningTeamMembers() {
//        if(red.getAlive() == 0)
//            return blue.getMembers();
//        return red.getMembers();
//    }
//
//    private Set<Player> getLosingTeamMembers() {
//        if(red.getAlive() != 0)
//            return blue.getMembers();
//        return red.getMembers();
//    }
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="Player join"
//    @Override
//    public void tryJoin(Player player) {
//        if(isPrivate && !whiteList.contains(player))
//            return ;List.of(Style.ERROR + "You cannot join this game, you are" +
//            " not on the whitelist.");
//        if(players.contains(player))
//            return ;List.of(Style.ERROR + "You are already part of this game.");
//        if(gameState == State.COUNTDOWN)
//            return ;List.of(Style.ERROR + "You cannot join the game during " +
//            "countdown, please wait before retrying.");
//        if(maxPlayers <=players.size())
//            return ;List.of(Style.ERROR + "Cannot join game, the game is " +
//            "already full.");
//        players.add(player);
//        return ;//joinGame(player);
//    }
//    private List<String> joinGame(Player player) {
//        if(red.getDeadMembers().contains(player))
//            return joinDeadRed(player);
//        if(blue.getDeadMembers().contains(player))
//            return joinDeadBlue(player);
//        return(joinRandom(player));
//    }
//    private List<String> joinDeadRed(Player player){
//        red.getMembers().add(player);
//        TeamHandler.spawn(player, spectator);
//        player.setGameMode(GameMode.SPECTATOR);
//        return List.of(Color.RED + player.getName() + " has joined, but was already dead.");
//    }
//    private List<String> joinRed(Player player) {
//        Matchmaker.addMember(player,red);
//        TeamHandler.spawn(player,red);
//        player.setGameMode(GameMode.ADVENTURE);
//        updateScoreboard();
//        return List.of(Color.RED + player.getName() + "  has joined the red team!");
//    }
//    private List<String> joinDeadBlue(Player player){
//        blue.getMembers().add(player);
//        TeamHandler.spawn(player, spectator);
//        player.setGameMode(GameMode.SPECTATOR);
//        return List.of(Color.BLUE + player.getName() + " has joined, but was already dead.");
//    }
//    private List<String> joinBlue(Player player) {
//        Matchmaker.addMember(player, blue);
//        TeamHandler.spawn(player,blue);
//        player.setGameMode(GameMode.ADVENTURE);
//        updateScoreboard();
//        return List.of(Color.BLUE + player.getName() + "  has joined the blue team!");
//    }
//    private List<String> joinRandom(Player player){
//        //todo: ELO shit
//        if(blue.getAlive() <=red.getAlive())
//            return joinBlue(player);
//        return joinRed(player);
//    }
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="Player leave"
//    @Override
//    public void leaveGame(Player player) {
//        red.getMembers().remove(player);
//        blue.getMembers().remove(player);
//        player.getInventory().clear();
//        spectator.getMembers().add(player);
//        checkEnd();
//    }
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="Checks"
//    @Override
//    public boolean canStart() {
//        return !(red.getMembers().isEmpty() ||
//                blue.getMembers().isEmpty());
//    }
//    private void checkEnd() {
//        if(red.getAlive() == 0 || blue.getAlive() == 0)
//            end(false);
//    }
//    //</editor-fold>
//    private void updateScoreboard(){
//        Objective Points = scoreboard.getObjective("TDM Scoreboard");
//        if (Points != null) {
//            Points.getScore(ChatColor.BLUE + "Blue:").setScore(blue.getAlive());
//            Points.getScore(ChatColor.DARK_RED + "Red:").setScore(red.getAlive());
//        }
//        else
//            Logger.getLogger("PVPPlugin").log(Level.SEVERE, "Scoreboard has failed for Team Deathmatch");
//    }
//    private class Listeners implements GamemodeListener {
//        HashMap<UUID, Long> playerAreaLeaveTimer = new HashMap<>();
//        @EventHandler
//        public void onPlayerDeath(PlayerDeathEvent e){
//            Player player = e.getEntity();
//            if(!players.contains(player))
//                return;
//            if(blue.getMembers().contains(player)){
//                blue.getDeadMembers().add(player);
//            }
//            if(red.getMembers().contains(player)){
//                red.getDeadMembers().add(player);
//            }
//            player.setGameMode(GameMode.SPECTATOR);
//            PlayerStatEditor.addDeath(player);
//            updateScoreboard();
//            checkEnd();
//            Player killer = player.getKiller();
//            if(killer != null){
//                PlayerStatEditor.addKill(killer);
//            }
//        }
//        @EventHandler
//        public void onPlayerLeave(PlayerQuitEvent e){
//            Player player = e.getPlayer();
//            red.getMembers().remove(player);
//            blue.getMembers().remove(player);
//            player.getInventory().clear();
//            players.remove(player);
//            updateScoreboard();
//            checkEnd();
//        }
//        @EventHandler
//        public void onPlayerMove(PlayerMoveEvent e) {
//            Location from = e.getFrom();
//            Location to = e.getTo();
//
//            Player player = e.getPlayer();
//            UUID uuid = player.getUniqueId();
//
//            if(gameState == State.QUEUED)
//                return;
//            if(gameState == State.COUNTDOWN &&
//                    !spectator.getMembers().contains(e.getPlayer()) &&
//                    (from.getX() != to.getX() || from.getZ() != to.getZ())) {
//                e.setCancelled(true);
//                return;
//            }
//
//            if(!region.contains(BlockVector3.at(to.getX(), to.getY(), to.getZ()))){
//                e.setCancelled(true);
//                if(!playerAreaLeaveTimer.containsKey(uuid)){
//                    player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
//                    playerAreaLeaveTimer.put(uuid, System.currentTimeMillis());
//                    return;
//                }
//                if(System.currentTimeMillis() - playerAreaLeaveTimer.get(uuid) > 3000){
//                    player.sendMessage(ChatColor.RED + "You aren't allowed to leave the map!");
//                    playerAreaLeaveTimer.put(uuid, System.currentTimeMillis());
//                }
//            }
//        }
//        @EventHandler
//        public void onPlayerJoin(PlayerJoinEvent e){
//            Player player = e.getPlayer();
//            if(PVPPlugin.getInstance().isPVPServer()) {
//                Matchmaker.addMember(player, spectator);
//                TeamHandler.spawn(player, spectator);
//            }
//        }
//
//    }
//    private static class TDMTeam extends Team{
//        Set<Player> deadMembers = new HashSet<>();
//
//        public Set<Player> getDeadMembers(){
//            return deadMembers;
//        }
//
//        public Integer getAlive(){
//            Set<Player> members = this.members;
//            members.removeAll(deadMembers);
//            return members.size();
//        }
//    }
}