package me.trixxtraxx.Practice.GameLogic;

import me.trixxtraxx.Practice.Gamemode.Game;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.world.*;

public class GameLogicListener implements Listener
{
    public void onEvent(Event e, World w, Player p)
    {
        Game g = Game.getGame(w);
        if(g== null) return;
        if(p == null)
        {
            g.getLogic().triggerEvent(e);
        }
        else
        {
            if(g.getLogic().getPlayers().contains(p)) {
                g.getLogic().triggerEvent(e);
            }
        }
    }

    public void onEvent(Event e, World w, Entity en)
    {
        Game g = Game.getGame(w);
        if( g== null) return;
        if(en instanceof Player)
        {
            Player p = (Player) en;
            if(g.getLogic().getPlayers().contains(p)) {
                g.getLogic().triggerEvent(e);
            }
        }
        else
        {
            g.getLogic().triggerEvent(e);
        }
    }

    @EventHandler
    public void onEventList(BlockBreakEvent e) {onEvent( e, e.getBlock().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(BlockBurnEvent e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockDamageEvent  e) {onEvent( e, e.getBlock().getWorld(),e.getPlayer());}
    @EventHandler
    public void onEventList(BlockDispenseEvent  e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockFadeEvent e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockFormEvent  e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockFromToEvent  e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockGrowEvent e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockIgniteEvent  e) {onEvent( e, e.getBlock().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(BlockPhysicsEvent   e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockPistonExtendEvent e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockPistonRetractEvent   e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(BlockPlaceEvent  e) {onEvent( e, e.getBlock().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(BlockRedstoneEvent  e) {onEvent( e, e.getBlock().getWorld(),null);}
    @EventHandler
    public void onEventList(BlockSpreadEvent e) {onEvent( e, e.getBlock().getWorld(),null);}
    @EventHandler
    public void onEventList(EntityBlockFormEvent   e) {onEvent( e, e.getBlock().getWorld(),null);}
    @EventHandler
    public void onEventList(LeavesDecayEvent  e) {onEvent( e, e.getBlock().getWorld(),null);}
    @EventHandler
    public void onEventList(NotePlayEvent  e) {onEvent( e, e.getBlock().getWorld(),null);}
    @EventHandler
    public void onEventList(SignChangeEvent  e) {onEvent( e, e.getBlock().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(EnchantItemEvent  e) {onEvent( e, e.getView().getPlayer().getWorld(), e.getView().getPlayer());}
    @EventHandler
    public void onEventList(PrepareItemEnchantEvent   e) {onEvent( e, e.getView().getPlayer().getWorld(), e.getView().getPlayer());}
    @EventHandler
    public void onEventList(CreatureSpawnEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityBreakDoorEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityChangeBlockEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityCombustByBlockEvent   e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityCombustByEntityEvent   e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityCombustEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityCreatePortalEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityDamageByBlockEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityDamageByEntityEvent   e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityDamageEvent   e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityDeathEvent  e) {if(!(e.getEntity() instanceof Player)) onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityExplodeEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityInteractEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityPortalEnterEvent e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityRegainHealthEvent   e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityShootBowEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityTameEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityTargetEvent e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityTargetLivingEntityEvent   e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(EntityTeleportEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(ExpBottleEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(ExplosionPrimeEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(FoodLevelChangeEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(ItemDespawnEvent   e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(PigZapEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(PlayerDeathEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(PotionSplashEvent e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(SheepDyeWoolEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(SheepRegrowWoolEvent e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(SlimeSplitEvent  e) {onEvent( e, e.getEntity().getWorld(), e.getEntity());}
    @EventHandler
    public void onEventList(BrewEvent   e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(CraftItemEvent  e) {onEvent( e, e.getWhoClicked().getWorld(), e.getWhoClicked());}
    @EventHandler
    public void onEventList(FurnaceBurnEvent  e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(FurnaceSmeltEvent  e) {onEvent( e, e.getBlock().getWorld(), null);}
    @EventHandler
    public void onEventList(InventoryClickEvent   e) {onEvent( e, e.getWhoClicked().getWorld(), e.getWhoClicked());}
    @EventHandler
    public void onEventList(InventoryCloseEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(InventoryEvent   e) {onEvent( e, e.getView().getPlayer().getWorld(), e.getView().getPlayer());}
    @EventHandler
    public void onEventList(InventoryOpenEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(AsyncPlayerChatEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerAnimationEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerBedEnterEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerBedLeaveEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerBucketEmptyEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerBucketFillEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerChannelEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerChatTabCompleteEvent e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerCommandPreprocessEvent e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerDropItemEvent e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerEggThrowEvent e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerExpChangeEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerFishEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerGameModeChangeEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerInteractEntityEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerInteractEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerItemBreakEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerItemHeldEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerJoinEvent e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerQuitEvent e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerKickEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerLevelChangeEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerLoginEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerMoveEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerPickupItemEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerPortalEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerRegisterChannelEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerRespawnEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerShearEntityEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerTeleportEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerToggleFlightEvent e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerToggleSneakEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerToggleSprintEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerUnregisterChannelEvent  e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(PlayerVelocityEvent   e) {onEvent( e, e.getPlayer().getWorld(), e.getPlayer());}
    @EventHandler
    public void onEventList(VehicleBlockCollisionEvent  e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(VehicleCreateEvent  e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(VehicleDamageEvent  e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(VehicleDestroyEvent  e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(VehicleEnterEvent  e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(VehicleExitEvent   e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(VehicleMoveEvent  e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(VehicleUpdateEvent  e) {onEvent( e, e.getVehicle().getWorld(), e.getVehicle());}
    @EventHandler
    public void onEventList(LightningStrikeEvent   e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(ThunderChangeEvent  e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(WeatherChangeEvent   e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(ChunkLoadEvent   e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(ChunkPopulateEvent   e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(ChunkUnloadEvent  e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(PortalCreateEvent   e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(SpawnChangeEvent   e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(StructureGrowEvent  e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList (WorldLoadEvent e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(WorldSaveEvent  e) {onEvent( e, e.getWorld(),null);}
    @EventHandler
    public void onEventList(WorldUnloadEvent  e) {onEvent( e, e.getWorld(),null);}
}
