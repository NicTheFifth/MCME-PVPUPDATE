package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.command.CommandUtil;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.SpawnListEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions.TeamSpawnListEditor;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mojang.brigadier.context.CommandContext;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

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
        Optional<MapEditor> result = getMapEditor(player);

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
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setArea(player);
            return 1;
        }
        return 0;
    }
    public static int SetTitle(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String mapName = c.getArgument(ArgumentNames.MAP_NAME, String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setTitle(mapName, player);
            return 1;
        }
        return 0;
    }
    public static int SetRP(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String rpName = c.getArgument(ArgumentNames.RESOURCEPACK, String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setRP(rpName,player);
            return 1;
        }
        return 0;
    }
    public static int SetGamemode(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        String gamemode = c.getArgument(ArgumentNames.GAMEMODE, String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setGamemodeEditor(gamemode, player);
            return 1;
        }
        return 0;
    }
    public static int SetMax(@NotNull CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        Integer max = c.getArgument(ArgumentNames.MAX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().getGamemodeEditor().setMaxPlayers(max, player);
            return 1;
        }
        return 0;
    }
    public static int setMapSpawn(@NotNull CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setSpawn(player);
            return 1;
        }
        return 0;
    }
    public static int SpawnListAddSpawn(CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((SpawnListEditor)result.get().getGamemodeEditor()).addSpawn(player);
            return 1;
        }
        return 0;
    }
    public static int SpawnListDeleteSpawn(CommandContext<McmeCommandSender> c) {
        Player player =  CommandUtil.getPlayer(c.getSource());
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((SpawnListEditor)result.get().getGamemodeEditor())
                .deleteSpawn(index, player);
            return 1;
        }
        return 0;
    }
    public static int TeamSpawnSetSpawn(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((TeamSpawnEditor)result.get().getGamemodeEditor())
                .getSpawnNames().get(spawnName).accept(player);
            return 1;
        }
        return 0;
    }
    public static int TeamSpawnListAddSpawn(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((TeamSpawnListEditor)result.get().getGamemodeEditor())
                .getSpawnListNames().get(spawnName)
                .addSpawn.accept(player);
            return 1;
        }
        return 0;
    }
    public static int TeamSpawnListDeleteSpawn(CommandContext<McmeCommandSender> c){
        Player player =  CommandUtil.getPlayer(c.getSource());
        String spawnName = c.getArgument(ArgumentNames.GAMEMODE_SPAWN,
            String.class);
        Integer index = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((TeamSpawnListEditor)result.get().getGamemodeEditor())
                .getSpawnListNames().get(spawnName)
                .deleteSpawn.apply(player).accept(index);
            return 1;
        }
        return 0;
    }
    /**
     *
     * @param player A player of whom their map editor is requested.
     * @return Optional&lt;MapEditor&gt; - Returns the empty optional when no
     * map
     * editor is found of the player or the found map editor.
     *
     */
    public static Optional<MapEditor> getMapEditor(@NotNull Player player) {
        MapEditor me = PVPPlugin.getInstance().getMapEditors().get(player.getUniqueId());
        if(me == null) {
            sendBaseComponent(new ComponentBuilder("Please select which map you " +
                "wish to edit with /mapedit <map name>")
                .color(Style.INFO)
                .create(),player);
            return Optional.empty();
        }
        return Optional.of(me);
    }
}
