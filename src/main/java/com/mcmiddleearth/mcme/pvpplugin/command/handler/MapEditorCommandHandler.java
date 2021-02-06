package com.mcmiddleearth.mcme.pvpplugin.command.handler;

import com.mcmiddleearth.mcme.pvpplugin.Maps.Map;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.Util.EventLocation;
import com.mcmiddleearth.mcme.pvpplugin.Util.Permission;
import com.mcmiddleearth.mcme.pvpplugin.command.argument.*;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.mcme.pvpplugin.command.builder.HelpfulRequiredArgumentBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Region;
import net.md_5.bungee.api.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapEditorCommandHandler extends AbstractCommandHandler{
    //TODO: Make the full command tree
    public MapEditorCommandHandler (String commandName, CommandDispatcher<CommandSender> dispatcher){
        super(commandName);
        dispatcher
            /*.register(HelpfulLiteralBuilder.literal(commandName)
                .withHelpText("Use this to create/edit new maps!")
                .withTooltip("Usage is to create new maps or edit existing maps!")
                .requires(commandSender -> commandSender.hasPermission(Permission.MAP_EDITOR))
                .then(HelpfulLiteralBuilder.literal("create")
                    .withHelpText("Create a new map!")
                    .withTooltip("Put in a name to create a new map!")
                )
            );*/
            .register(HelpfulLiteralBuilder.literal(commandName)
                    .requires(commandSender -> commandSender.hasPermission(Permission.PVP_ADMIN))
                    .then(HelpfulLiteralBuilder.literal("create"))
                    .then(HelpfulRequiredArgumentBuilder.argument("map", new MapAbbreviationArgumentType())
                            .then(HelpfulLiteralBuilder.literal("setarea")
                               .executes(command -> {
                                   setArea(command.getArgument("map", String.class), (Player)command.getSource());
                                   return 0;
                               }))
                            .then(HelpfulLiteralBuilder.literal("settitle")
                                    .executes(command -> {
                                        setMapTitle(command.getArgument("map", String.class),command.getArgument("title", String.class), (Player)command.getSource());
                                        return 0;
                                    }))
                            .then(HelpfulLiteralBuilder.literal("setspawn").executes(command -> {
                                return 0;
                            }))
                            .then(HelpfulLiteralBuilder.literal("setrp")
                                    .executes(command -> {
                                        setRP(command.getArgument("map", String.class),command.getArgument("rp", String.class), (Player)command.getSource());
                                        return 0;
                                    }))
                            .then(HelpfulLiteralBuilder.literal("addgamemode")))
                            .then(HelpfulRequiredArgumentBuilder.argument("addgamemode", new GamemodeArgumentType())
                                    .executes(command -> {
                                        setGamemode(command.getArgument("map", String.class),command.getArgument("gamemode", String.class), (Player)command.getSource());
                                        return 0;
                                    })
                                    .then(HelpfulLiteralBuilder.literal("maxPlayers")
                                        .then(HelpfulRequiredArgumentBuilder.argument("maxPlayers", new IntArgumentType()))
                                            .executes(command -> {
                                                setMaxPlayers(command.getArgument("map", String.class),command.getArgument("maxPlayers", String.class), (Player)command.getSource());
                                                return 0;
                                            })
                                    .then(HelpfulLiteralBuilder.literal("spawn")
                                            .then(HelpfulLiteralBuilder.literal("show")
                                                    .executes(command -> {
                                                        showSpawns(command.getArgument("map", String.class), (Player)command.getSource());
                                                        return 0;
                                                    }))
                                            .then(HelpfulLiteralBuilder.literal("hide")
                                                    .executes(command -> {
                                                        hideSpawns((Player)command.getSource());
                                                        return 0;
                                                    }))
                                            .then(HelpfulLiteralBuilder.literal("list"))
                                            .then(HelpfulRequiredArgumentBuilder.argument("set", new SpawnNameArgumentType()))
                                    )
                                    .then(HelpfulRequiredArgumentBuilder.argument("setPOI", new POINameArgumentType()))
                            )
                    )
                    );

    }
    public static void setArea(String map, Player p){
        Map m = Map.maps.get(map);
        BukkitPlayer bukkitP = new BukkitPlayer(p);//PVPCore.getWorldEditPlugin(), PVPCore.getWorldEditPlugin().getServerInterface(), p);
        LocalSession session = PVPPlugin.getWorldEditPlugin().getWorldEdit().getSessionManager().get(bukkitP);

        try{
            Region r = session.getSelection(new BukkitWorld(p.getWorld()));
            if(r.getHeight() < 250){
                p.sendMessage(ChatColor.RED + "I think you forgot to do //expand vert!");
            }
            else{
                List<BlockVector2> wePoints = r.polygonize(1000);
                ArrayList<EventLocation> bPoints = new ArrayList<>();

                for(BlockVector2 point : wePoints){
                    bPoints.add(new EventLocation(new Location(p.getWorld(), point.getX(), 1, point.getZ())));
                }

                m.setRegionPoints(bPoints);
                m.initializeRegion();
                p.sendMessage(ChatColor.YELLOW + "Area set!");
            }
        }
        catch(IncompleteRegionException e){
            p.sendMessage(ChatColor.RED + "You don't have a region selected!");
        }
    }

    public static void setMapTitle(String map, String title, Player p){
        Map m = com.mcmiddleearth.mcme.pvpplugin.Maps.Map.maps.get(map);
        m.setTitle(title);
        sendMapMessage(map, m, p);
    }
    public static void setRP(String map, String rp, Player p){
        Map m = Map.maps.get(map);
        switch(rp) {
            case "eriador":
                m.setResourcePackURL("http://www.mcmiddleearth.com/content/Eriador.zip");
                break;
            case "rohan":
                m.setResourcePackURL("http://www.mcmiddleearth.com/content/Rohan.zip");
                break;
            case "lothlorien":
                m.setResourcePackURL("http://www.mcmiddleearth.com/content/Lothlorien.zip");
                break;
            case "gondor":
                m.setResourcePackURL("http://www.mcmiddleearth.com/content/Gondor.zip");
                break;
            case "moria":
                m.setResourcePackURL("http://www.mcmiddleearth.com/content/Moria.zip");
                break;
            case "mordor":
                m.setResourcePackURL("http://www.mcmiddleearth.com/content/Mordor.zip");
                break;
        }
        sendMapMessage(map, m, p);
    }

    public static void setGamemode(String map, String gamemode, Player p){
        Map m = Map.maps.get(map);
        m.setGmType(gamemode);
        m.bindGamemode();
        sendMapMessage(map, m, p);
    }

    public static void setMaxPlayers(String map, String amount, Player p){
        Map m = com.mcmiddleearth.mcme.pvp.maps.Map.maps.get(map);
        m.setMax(Integer.parseInt(amount));
        sendMapMessage(map, m, p);
    }
    public static void showSpawns(String map, Player p){
        Map m = Map.maps.get(map);
        EventLocation loc;
        for(String name : m.getImportantPoints().keySet()){
            loc = m.getImportantPoints().get(name);
            ArmorStand marker = (ArmorStand) loc.toBukkitLoc().getWorld().spawnEntity(loc.toBukkitLoc().add(0, 1, 0), EntityType.ARMOR_STAND);
            marker.setGravity(false);
            marker.setCustomName(name);
            marker.setCustomNameVisible(true);
            marker.setGlowing(true);
            marker.setMarker(true);
        }
    }

    public static void hideSpawns(Player p){
        ArmorStand toDelete;
        for(Entity marker : Objects.requireNonNull(PVPPlugin.getSpawn().getWorld()).getEntities())
            if(marker.getType() == EntityType.ARMOR_STAND){
                toDelete = (ArmorStand) marker;
                if(toDelete.isMarker())
                    toDelete.remove();
            }
    }


}
