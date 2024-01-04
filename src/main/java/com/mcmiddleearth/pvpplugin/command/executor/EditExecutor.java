package com.mcmiddleearth.pvpplugin.command.executor;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.PVPPlugin;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.DeathRunEditor;
import com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.TeamConquestEditor;
import com.mcmiddleearth.pvpplugin.statics.ArgumentNames;
import com.mcmiddleearth.pvpplugin.json.jsonData.JSONMap;
import com.mcmiddleearth.pvpplugin.mapeditor.MapEditor;
import com.mojang.brigadier.context.CommandContext;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.Optional;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public class EditExecutor {
            //Structure function
            /*public static int Action(CommandContext<McmeCommandSender> c){
                Player player = (Player) c.getSource();
                Optional<MapEditor> result = getMapEditor(player);

                if(result.isPresent()) {
                    result.get().Action(player);
                    return 1;
                }
                return 0;
            }*/
    public static int CreateMap(CommandContext<McmeCommandSender> c){
        Player player = (Player) c.getSource();
        String mapName = c.getArgument(ArgumentNames.MAP_NAME, String.class);

        JSONMap newMap = new JSONMap(mapName);
        PVPPlugin.getInstance().getMaps().put(mapName, newMap);
        PVPPlugin.getInstance().getMapEditors().put(player.getUniqueId(),
            new MapEditor(newMap, player));
        return 1;
    }
    public static int SelectMap(CommandContext<McmeCommandSender> c){
        Player player = (Player) c.getSource();
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
    public static int SetArea(CommandContext<McmeCommandSender> c){
        Player player = (Player) c.getSource();
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setArea(player);
            return 1;
        }
        return 0;
    }
    public static int SetTitle(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        String mapName = c.getArgument(ArgumentNames.MAP_NAME, String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setTitle(mapName, player);
            return 1;
        }
        return 0;
    }
    public static int SetRP(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        String rpName = c.getArgument(ArgumentNames.RESOURCEPACK, String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setRP(rpName,player);
            return 1;
        }
        return 0;
    }
    public static int SetGamemode(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        String gamemode = c.getArgument("gamemode", String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setGamemodeEditor(gamemode, player);
            return 1;
        }
        return 0;
    }
    public static int SetMax(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        Integer max = c.getArgument("amount", Integer.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().getGamemodeEditor().setMaxPlayers(max, player);
            return 1;
        }
        return 0;
    }

    /**
     * This is used with the condition that the gamemode editor is set to the
     * deathrun editor.
     * @param c context needed to do the required action.
     * @return int - 1 if finished successfully or 0 if failed.
     */
    public static int EditGoal(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((DeathRunEditor)result.get().getGamemodeEditor()).setGoal(player);
            return 1;
        }
        return 0;
    }
    /**
     * This is used with the condition that the gamemode editor is set to the
     * deathrun editor.
     * @param c context needed to do the required action.
     * @return int - 1 if finished successfully or 0 if failed.
     */
    public static int CreateCapture(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((TeamConquestEditor)result.get().getGamemodeEditor())
                .AddCapturePoint(player);
            return 1;
        }
        return 0;
    }
    public static int DelCapture(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        Integer point = c.getArgument(ArgumentNames.INDEX, Integer.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            ((TeamConquestEditor)result.get().getGamemodeEditor())
                .DeleteGoal(point, player);
            return 1;
        }
        return 0;
    }
    public static int setMapSpawn(CommandContext<McmeCommandSender> c){
        Player player = (Player) c.getSource();
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            result.get().setSpawn(player);
            return 1;
        }
        return 0;
    }
    public static int setSpawn(CommandContext<McmeCommandSender> c) {
        Player player = (Player) c.getSource();
        String spawnType = c.getArgument(ArgumentNames.GAMEMODE_SPAWN, String.class);
        Optional<MapEditor> result = getMapEditor(player);

        if(result.isPresent()) {
            //result.get().Action(player);
            switch(spawnType) {
                case "red":
                    //TODO: Check instance of the GamemodeEditor, to make sure it can edit
                    //response = me.setRedSpawn(source.getLocation());
                    break;
                case "blue":

                    //TODO: Check instance of the GamemodeEditor, to make sure it can edit
                    //response = me.setBlueSpawn(source.getLocation());
                    break;
                case "death":

                    //TODO: Check instance of the GamemodeEditor, to make sure it can edit
                    //response = me.setDeathSpawn(source.getLocation());
                    break;
                case "runner":

                    //TODO: Check instance of the GamemodeEditor, to make sure it can edit
                    //response = me.setRunnerSpawn(source.getLocation());
                    break;
                case "infected":

                    //TODO: Check instance of the GamemodeEditor, to make sure it can edit
                    //response = me.setInfectedSpawn(source.getLocation());
                    break;
                case "survivor":

                    //TODO: Check instance of the GamemodeEditor, to make sure it can edit
                    //response = me.setSurvivorSpawn(source.getLocation());
                    break;
                default:
                    player.sendMessage();
                    return 0;
            }
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
    public static Optional<MapEditor> getMapEditor(Player player) {
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
