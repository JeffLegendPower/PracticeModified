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

public class LobbyListener implements Listener {

    private Lobby lobby;

    public LobbyListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(lobby.getWorld() == event.getPlayer().getWorld() && !lobby.getCanPlaceBlocks()) {
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(lobby.getWorld() == event.getPlayer().getWorld() && !lobby.getCanBreakBlocks()){
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(PlayerBucketEmptyEvent event) {
        if(lobby.getWorld() == event.getPlayer().getWorld() && !lobby.getCanPlaceBlocks()){
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(PlayerBucketFillEvent event)
    {
        if(lobby.getWorld() == event.getPlayer().getWorld() && !lobby.getCanBreakBlocks()){
            if(event.getPlayer().hasPermission("practice.lobby.bypass") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if(lobby.getWorld() != event.getEntity().getWorld()) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.CONTACT)
            if(!lobby.getAllowEntityDamage())
                event.setCancelled(true);
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent event) {
        if(lobby.getWorld() != event.getPlayer().getWorld()) return;
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer((Player) event.getPlayer())) return;
        if(lobby.getBlockedInventories().contains(event.getInventory().getTitle()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(lobby.getWorld() != event.getPlayer().getWorld()) return;
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(event.getPlayer())) return;
        Practice.log(4, "Player interacted with Item in lobby");
        PracticePlayer pp = PracticePlayer.getPlayer(event.getPlayer());
        if(!pp.isInQueue()) {
            LobbyItem item = lobby.getItem((event.getPlayer().getInventory().getHeldItemSlot()));
            if(item != null)
                item.onClick(event);
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
    public void onEntityInteract(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(lobby.getWorld() != event.getEntity().getWorld()) return;
        Player p = (Player) event.getDamager();
        LobbyItem item = lobby.getItem(p.getInventory().getHeldItemSlot());
        PracticePlayer pp = PracticePlayer.getPlayer(p);
        if(!pp.isInQueue())
            if(item != null)
                item.onClick(event);
    }

    @EventHandler
    public void onInvMove(InventoryClickEvent event) {
        if(lobby.getWorld() != event.getWhoClicked().getWorld()) return;
        if(KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer((Player) event.getWhoClicked())) return;
        if(!lobby.getAllowEditingInventory()) {
            if(event.getWhoClicked().hasPermission("practice.lobby.bypass") && event.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyLeave(PlayerQuitEvent event) {
        if(lobby.getWorld() != event.getPlayer().getWorld()) return;
        lobby.removePlayer(PracticePlayer.getPlayer(event.getPlayer()), true);
    }

    @EventHandler
    public void onLobbyLeave(PlayerChangedWorldEvent event) {
        if(lobby.getWorld() != event.getFrom()) return;
        lobby.removePlayer(PracticePlayer.getPlayer(event.getPlayer()), true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLobbyJoin(PlayerChangedWorldEvent event)
    {
        if(lobby.getWorld() != event.getPlayer().getWorld()) return;
        PracticePlayer pp = PracticePlayer.getPlayer(event.getPlayer());
        lobby.addPlayer(pp);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLobbyJoin(PlayerJoinEvent event)
    {
        if(lobby.getWorld() != event.getPlayer().getWorld()) return;
        PracticePlayer pp = PracticePlayer.getPlayer(event.getPlayer());
        lobby.addPlayer(pp);
    }

    @EventHandler
    public void onEntityDamage(PlayerDropItemEvent event) {
        if(lobby.getWorld() != event.getPlayer().getWorld()) return;
        if(lobby.getCanDropItems())
            event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(lobby.getWorld() != event.getEntity().getWorld()) return;
        if(lobby.getLockHunger())
            event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        if(lobby.getWorld() != event.getWorld()) return;
        if(lobby.getLockWeather())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(lobby.getWorld() != event.getPlayer().getWorld()) return;
        if(event.getTo().getY() < lobby.getVoidOutHeight()) {
            event.getPlayer().teleport(lobby.getSpawn());
        }
        for(Launchpad l : lobby.getLaunchpads()) {
            l.tryLaunch(event.getPlayer());
        }
    }
}