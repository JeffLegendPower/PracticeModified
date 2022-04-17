package me.trixxtraxx.Practice.Lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class LobbyListener implements Listener
{
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Lobby lobby = Lobby.get(event.getBlock().getWorld());
        if(lobby != null &&  lobby.isPlaceBlocked()){
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockPlaceEvent event){
        Lobby lobby = Lobby.get(event.getBlock().getWorld());
        if(lobby != null &&  lobby.isBreakBlocked()){
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        Lobby lobby = Lobby.get(event.getEntity().getWorld());
        if(lobby == null) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.CONTACT)
        {
            if(lobby.isEntityDamageBlocked())
            {
                event.setCancelled(true);
            }
        }
        else
        {
            if(lobby.isDamageBlocked())
            {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onInvOpen(InventoryOpenEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        if(lobby.getBlockedInventories().contains(event.getInventory().getTitle()))
        {
            event.setCancelled(true);
        }
    }
}
