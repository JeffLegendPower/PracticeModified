package me.trixxtraxx.Practice.Kit.Editor;

import me.trixxtraxx.Practice.Map.Editor.MapEditingSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public class KitEditorListener implements Listener
{
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(KitEditor.hasInstance()) KitEditor.getInstance().playerMove(e);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent event)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(event.getPlayer())) KitEditor.getInstance().remove(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerChangedWorldEvent event)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(event.getPlayer())) KitEditor.getInstance().remove(event.getPlayer());
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(e.getPlayer())) e.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(e.getPlayer())) e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player && KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer((Player) e.getEntity())) e.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(PlayerBucketEmptyEvent e)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(e.getPlayer())) e.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(PlayerBucketFillEvent e)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(e.getPlayer())) e.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(PlayerBedEnterEvent e)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(e.getPlayer())) e.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(PlayerDropItemEvent e)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(e.getPlayer())) e.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(PlayerPickupItemEvent e)
    {
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(e.getPlayer())) e.setCancelled(true);
    }
}
