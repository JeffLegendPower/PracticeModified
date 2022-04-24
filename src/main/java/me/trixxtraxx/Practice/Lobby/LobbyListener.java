package me.trixxtraxx.Practice.Lobby;

import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

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
    public void onBlockBreak(BlockBreakEvent event){
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
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        LobbyItem item = lobby.getItem((event.getPlayer().getInventory().getHeldItemSlot()));
        if(item != null)
        {
            item.onClick(event);
        }
    }
    
    @EventHandler
    public void onInvMove(InventoryClickEvent event)
    {
        Lobby lobby = Lobby.get(event.getWhoClicked().getWorld());
        if(lobby == null) return;
        if(lobby.isInvMoveBlocked())
        {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLobbyLeave(PlayerQuitEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        lobby.removePlayer(PracticePlayer.getPlayer(event.getPlayer()));
    }
    
    @EventHandler
    public void onLobbyLeave(PlayerChangedWorldEvent event)
    {
        Lobby lobby = Lobby.get(event.getFrom());
        if(lobby == null) return;
        lobby.removePlayer(PracticePlayer.getPlayer(event.getPlayer()));
    }
    
    @EventHandler
    public void onLobbyJoin(PlayerChangedWorldEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        lobby.addPlayer(PracticePlayer.getPlayer(event.getPlayer()));
    }
    
    @EventHandler
    public void onLobbyJoin(PlayerJoinEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        lobby.addPlayer(PracticePlayer.getPlayer(event.getPlayer()));
    }
    
    @EventHandler
    public void onEntityDamage(PlayerDropItemEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        if(lobby.isDropBlocked())
        {
            event.setCancelled(true);
        }
    }
}
