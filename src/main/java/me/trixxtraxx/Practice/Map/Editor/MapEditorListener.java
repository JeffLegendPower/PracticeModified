package me.trixxtraxx.Practice.Map.Editor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MapEditorListener implements Listener
{
    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        MapEditingSession.removeSession(MapEditingSession.getSession(event.getPlayer()));
    }

    @EventHandler
    public void onLeave(PlayerChangedWorldEvent event)
    {
        MapEditingSession.removeSession(MapEditingSession.getSession(event.getPlayer()));
    }


}
