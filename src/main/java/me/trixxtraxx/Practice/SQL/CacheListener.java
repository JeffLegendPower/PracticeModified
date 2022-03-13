package me.trixxtraxx.Practice.SQL;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CacheListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        PracticePlayer.generatePlayer(e.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerQuitEvent e)
    {
        PracticePlayer.removePlayer(e.getPlayer());
    }
}