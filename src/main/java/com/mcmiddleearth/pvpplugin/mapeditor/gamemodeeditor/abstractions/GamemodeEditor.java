package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import com.mcmiddleearth.command.Style;
import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONGamemode;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import static com.mcmiddleearth.pvpplugin.command.CommandUtil.sendBaseComponent;

public abstract class GamemodeEditor {
    protected JSONGamemode jsonGamemode;
    protected String gamemode;
    private String displayString;
    public void setMaxPlayers(Integer maxPlayers, Player player){
        jsonGamemode.setMaximumPlayers(maxPlayers);
        sendBaseComponent(
            new ComponentBuilder(String.format("Set the max players to %d.",
                maxPlayers))
                .color(Style.INFO)
                .create(),
            player);
    }
    protected void setDisplayString(String displayString){
        this.displayString = displayString;
    }
    protected String getDisplayString(){
        return this.displayString;
    }
    public String getGamemode() {
        return gamemode;
    }
    public abstract void sendStatus(Player player);
}
