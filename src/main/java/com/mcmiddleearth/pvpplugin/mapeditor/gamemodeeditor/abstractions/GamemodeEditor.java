package com.mcmiddleearth.pvpplugin.mapeditor.gamemodeeditor.abstractions;

import com.mcmiddleearth.pvpplugin.json.jsonData.jsonGamemodes.abstractions.JSONGamemode;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;


public abstract class GamemodeEditor {
    protected JSONGamemode jsonGamemode;
    protected String gamemode;
    private String displayString;
    public void setMaxPlayers(Integer maxPlayers, Player player){
        jsonGamemode.setMaximumPlayers(maxPlayers);
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                "<aqua>Set the max players to <amount>.</aqua>",
                Placeholder.parsed("amount", String.valueOf(maxPlayers))));
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

    public abstract void ShowPoints(Player player);
}
