package me.trixxtraxx.Practice;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PracticeListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }
    
    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event){
        event.setDeathMessage(null);
    }
}
