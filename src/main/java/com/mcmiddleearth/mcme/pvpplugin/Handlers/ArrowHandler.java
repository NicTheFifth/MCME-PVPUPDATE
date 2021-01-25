package com.mcmiddleearth.mcme.pvpplugin.Handlers;


import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class ArrowHandler implements Listener{

    public static void initializeArrowHandling(){
        Bukkit.getPluginManager().registerEvents(new ArrowHandler(), PVPPlugin.getPlugin());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PVPPlugin.getPlugin(), despawnArrows, 0, 5);
    }

    public static Runnable despawnArrows = new Runnable(){
        @Override
        public void run(){
            if(Bukkit.getOnlinePlayers().size() > 0){
                Player p = (Player) Bukkit.getOnlinePlayers().toArray()[0];
                for(Arrow arrow : p.getWorld().getEntitiesByClass(Arrow.class)){
                    if(arrow.isOnGround()){
                        arrow.remove();
                    }}}}};

    @EventHandler
    public void onArrowPickup (PlayerPickupArrowEvent e){
        e.setCancelled(true);
    }
}
