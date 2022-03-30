package me.trixxtraxx.Practice.Kit.Editor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class KitEditorListener implements Listener
{
    //subscribe to PlayerMoveEvent and parse it to KitEditor
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(KitEditor.hasInstance()) KitEditor.getInstance().playerMove(e);
    }
}
