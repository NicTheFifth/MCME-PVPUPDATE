package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.DeathRunEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.*;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class EditExecutor {
            //Structure function
            /*public static int Action(CommandContext<McmeCommandSender> c){
                Player player = CommandUtil.getPlayer(c.getSource());
                Optional<MapEditor> result = getMapEditor(player);

                if(result.isPresent()) {
                    result.get().Action(player);
                    return 1;
                }
                return 0;
            }*/
    public static int CreateMap(@NotNull CommandContext<McmeCommandSender> c){
        Player player = CommandUtil.getPlayer(c.getSource());
        String mapName = c.getArgument(ArgumentNames.MAP_NAME, String.class);

        JSONMap newMap = new JSONMap(mapName);
        PVPPlugin.getInstance().getMaps().put(mapName, newMap);
        PVPPlugin.getInstance().getMapEditors().put(player.getUniqueId(),
            new MapEditor(newMap, player));
        return 1;
    }

    public static int SelectMap(@NotNull CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String mapName = c.getArgument(ArgumentNames.MAP_NAME, String.class);
        Optional<MapEditor> result = getMapEditor(player, false);

        if(result.isPresent()) {
            result.get().setMap(mapName, player);
            return 1;
        }
        JSONMap map = PVPPlugin.getInstance().getMaps().get(mapName);
        PVPPlugin.getInstance().getMapEditors().put(player.getUniqueId(),
            new MapEditor(map, player));
        return 1;
    }

    public static int SetArea(@NotNull CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().setArea(player);
            return 1;
        }
        return 0;
    }

    public static int SetTitle(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String mapName = c.getArgument(ArgumentNames.MAP_NAME, String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().setTitle(mapName, player);
            return 1;
        }
        return 0;
    }

    public static int SetRP(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String rpName = c.getArgument(ArgumentNames.RESOURCEPACK, String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().setRP(rpName,player);
            return 1;
        }
        return 0;
    }

    public static int SetGamemode(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().setGamemodeEditor(gamemode, player);
            return 1;
        }
        return 0;
    }

    public static int SetMax(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        Integer max = c.getArgument(ArgumentNames.MAX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().getGamemodeEditor().setMaxPlayers(max, player);
            return 1;
        }
        return 0;
    }

    public static int setMapSpawn(@NotNull CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().setSpawn(player);
            return 1;
        }
        return 0;
    }
    public static int SetKillHeight(CommandContext<McmeCommandSender> c){
        Player player = CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((DeathRunEditor)result.get().getGamemodeEditor()).setKillHeight(player);
            return 1;
        }
        return 0;
    }
    public static int SpawnListAddSpawn(CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((SpawnListEditor)result.get().getGamemodeEditor()).addSpawn(player);
            return 1;
        }
        return 0;
    }

    public static int SpawnListDeleteSpawn(CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((SpawnListEditor)result.get().getGamemodeEditor())
                .deleteSpawn(index, player);
            return 1;
        }
        return 0;
    }

    public static int SpawnListTeleport(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((SpawnListEditor)result.get().getGamemodeEditor())
                .teleportToSpawn(player, index);
            return 1;
        }
        return 0;
    }

    public static int TeamSpawnSetSpawn(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((TeamSpawnEditor)result.get().getGamemodeEditor())
                .getSpawnNames().get(spawnName).setSpawn(player);
            return 1;
        }
        return 0;
    }

    public static int TeamSpawnTeleporter(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isEmpty()) {
            return 0;
        }
        if(result.get().getGamemodeEditor() instanceof TeamSpawnEditor) {
            ((TeamSpawnEditor) result.get().getGamemodeEditor())
                .getSpawnNames().get(spawnName).Teleport(player);
            return 1;
        }
        player.sendMessage(Component.text()
                .content("Please add an index, you have a Team Spawn List editor")
                .color(NamedTextColor.RED).build());
        return 0;
    }

    public static int TeamSpawnListAddSpawn(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((TeamSpawnListEditor)result.get().getGamemodeEditor())
                .getSpawnListNames().get(spawnName)
                .addSpawn(player);
            return 1;
        }
        return 0;
    }

    public static int TeamSpawnListDeleteSpawn(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((TeamSpawnListEditor)result.get().getGamemodeEditor())
                .getSpawnListNames().get(spawnName)
                .deleteSpawn(player, index);
            return 1;
        }
        return 0;
    }

    public static int TeamSpawnListTeleporter(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((TeamSpawnListEditor)result.get().getGamemodeEditor())
                .getSpawnListNames().get(spawnName)
                .teleport(player, index);
            return 1;
        }
        return 0;
    }

    public static int SetSpecialPoint(CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String pointName = c.getArgument(ArgumentNames.SPECIAL_POINT,
            String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((SpecialPointEditor)result.get().getGamemodeEditor())
                .getSpecialPointNames().get(pointName).setPoint(player);
            return 1;
        }
        return 0;
    }

    public static int TeleportToSpecialPoint(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String pointName = c.getArgument(ArgumentNames.SPECIAL_POINT,
            String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isEmpty()) {
            return 0;
        }
        if(result.get() instanceof SpecialPointListEditor) {
            player.sendMessage(Component.text()
                    .content("Please add an index, you have a Special Point List editor")
                    .color(NamedTextColor.RED).build());
            return 0;
        }
        ((SpecialPointEditor)result.get().getGamemodeEditor())
            .getSpecialPointNames().get(pointName).Teleport(player);
        return 1;
    }

    public static int AddSpecialPoint(CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String pointName = c.getArgument(ArgumentNames.SPECIAL_POINT,
            String.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((SpecialPointListEditor)result.get().getGamemodeEditor())
                .getSpecialPointListNames().get(pointName)
                .addPoint(player);
            return 1;
        }
        return 0;
    }

    public static int DeleteSpecialPoint(CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String pointName = c.getArgument(ArgumentNames.SPECIAL_POINT,
            String.class);
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((SpecialPointListEditor)result.get().getGamemodeEditor())
                .getSpecialPointListNames().get(pointName)
                .deletePoint(player, index);
            return 1;
        }
        return 0;
    }

    public static int TeleportSpecialPointList(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String pointName = c.getArgument(ArgumentNames.SPECIAL_POINT,
            String.class);
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            ((SpecialPointListEditor) result.get().getGamemodeEditor())
                .getSpecialPointListNames().get(pointName)
                .teleport(player, index);
            return 1;
        }
        return 0;
    }

    public static int SendInfo(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().sendStatus(player);
            return 1;
        }
        return 0;
    }

    public static int SendGamemodeInfo(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().getGamemodeEditor().sendStatus(player);
            return 1;
        }
        return 0;
    }

    public static int ShowSpawns(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            result.get().showSpawns(player);
            return 1;
        }
        return 0;
    }

    public static int HideSpawns(CommandContext<McmeCommandSender> c) {
        Player player = CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player, true);

        if(result.isPresent()) {
            MapEditor.hideSpawns(player, true);
            return 1;
        }
        return 0;
    }

    /**
     *
     * @param player A player of whom their map editor is requested.
     * @return Optional&lt;MapEditor&gt; - Returns the empty optional when no
     * map editor is found of the player or the found map editor.
     *
     */
    public static Optional<MapEditor> getMapEditor(@NotNull Player player,
                                                   boolean sendMessage) {
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(player.getUniqueId());
        if(me == null) {
            if(sendMessage)
                player.sendMessage(Component.text()
                        .content("Please select which map you wish to edit with /mapedit <map name>")
                        .color(NamedTextColor.RED).build());
            return Optional.empty();
        }
        return Optional.of(me);
    }
}
