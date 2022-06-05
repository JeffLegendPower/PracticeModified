package me.trixxtraxx.Practice.Lobby;

import me.trixxtraxx.Practice.Kit.Editor.KitEditor;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class LobbyListener implements Listener
{
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Lobby lobby = Lobby.get(event.getBlock().getWorld());
        if(lobby != null &&  lobby.isPlaceBlocked())
        {
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Lobby lobby = Lobby.get(event.getBlock().getWorld());
        if(lobby != null &&  lobby.isBreakBlocked()){
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlace(PlayerBucketEmptyEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby != null &&  lobby.isPlaceBlocked()){
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlace(PlayerBucketFillEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby != null &&  lobby.isBreakBlocked()){
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
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
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer((Player) event.getPlayer())) return;
        if(lobby.getBlockedInventories().contains(event.getInventory().getTitle()))
        {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(event.getPlayer())) return;
        PracticePlayer pp = PracticePlayer.getPlayer(event.getPlayer());
        if(!pp.isInQueue())
        {
            LobbyItem item = lobby.getItem((event.getPlayer().getInventory().getHeldItemSlot()));
            if(item != null)
            {
                item.onClick(event);
            }
        }
        else
        {
            if(event.getPlayer().getInventory().getHeldItemSlot() == 8)
            {
                pp.leaveQueue();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityInteract(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof Player)) return;
        Lobby lobby = Lobby.get(event.getEntity().getWorld());
        if(lobby == null) return;
        Player p = (Player) event.getDamager();
        LobbyItem item = lobby.getItem(p.getInventory().getHeldItemSlot());
        PracticePlayer pp = PracticePlayer.getPlayer(p);
        if(!pp.isInQueue())
        {
            if(item != null)
            {
                item.onClick(event);
            }
        }
    }
    
    @EventHandler
    public void onInvMove(InventoryClickEvent event)
    {
        Lobby lobby = Lobby.get(event.getWhoClicked().getWorld());
        if(lobby == null) return;
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer((Player) event.getWhoClicked())) return;
        if(lobby.isInvMoveBlocked())
        {
            if(event.getWhoClicked().hasPermission("practice.lobby.bypass") && event.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLobbyLeave(PlayerQuitEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        lobby.removePlayer(PracticePlayer.getPlayer(event.getPlayer()), true);
    }
    
    @EventHandler
    public void onLobbyLeave(PlayerChangedWorldEvent event)
    {
        Lobby lobby = Lobby.get(event.getFrom());
        if(lobby == null) return;
        lobby.removePlayer(PracticePlayer.getPlayer(event.getPlayer()), true);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onLobbyJoin(PlayerChangedWorldEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        PracticePlayer pp = PracticePlayer.getPlayer(event.getPlayer());
        lobby.addPlayer(pp);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onLobbyJoin(PlayerJoinEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        PracticePlayer pp = PracticePlayer.getPlayer(event.getPlayer());
        lobby.addPlayer(pp);
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
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        Lobby lobby = Lobby.get(event.getEntity().getWorld());
        if(lobby == null) return;
        if(lobby.isHungerBlocked())
        {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        Lobby lobby = Lobby.get(event.getWorld());
        if(lobby == null) return;
        if(lobby.isWeatherBlocked())
        {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Lobby lobby = Lobby.get(event.getPlayer().getWorld());
        if(lobby == null) return;
        for(Launchpad l : lobby.getLaunchpads())
        {
            l.tryLaunch(event.getPlayer());
        }
    }
}
