package me.trixxtraxx.Practice.Lobby

import me.trixxtraxx.Practice.Kit.Editor.KitEditor
import me.trixxtraxx.Practice.Practice
import me.trixxtraxx.Practice.SQL.PracticePlayer
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*
import org.bukkit.event.weather.WeatherChangeEvent

class LobbyListener(private val lobby: Lobby) : Listener {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {

        if (lobby.world == event.player.world && !lobby.canPlaceBlocks) {
            if (event.player.hasPermission("practice.lobby.bypass") && event.player.gameMode == GameMode.CREATIVE) return
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (lobby.world == event.player.world && !lobby.canBreakBlocks) {
            if (event.player.hasPermission("practice.lobby.bypass") && event.player.gameMode == GameMode.CREATIVE) return
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: PlayerBucketEmptyEvent) {
        if (lobby.world == event.player.world && !lobby.canPlaceBlocks) {
            if (event.player.hasPermission("practice.lobby.bypass") && event.player.gameMode == GameMode.CREATIVE) return
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: PlayerBucketFillEvent) {
        if (lobby.world == event.player.world && !lobby.canBreakBlocks) {
            if (event.player.hasPermission("practice.lobby.bypass") && event.player.gameMode == GameMode.CREATIVE) return
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (lobby.world == event.entity.world && event.cause == EntityDamageEvent.DamageCause.CONTACT && !lobby.allowEntityDamage)
            event.isCancelled = true else if (!lobby.canDamageBlocks) event.isCancelled = true
    }

    @EventHandler
    fun onInvOpen(event: InventoryOpenEvent) {

        if (KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(event.player as Player)) return
        if (lobby.world == event.player.world && lobby.blockedInventories.contains(event.inventory.title)) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (lobby.world != event.player.world && KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(event.player)) return
        Practice.log(4, "Player interacted with Item in lobby")
        val pp = PracticePlayer.getPlayer(event.player)
        if (!pp.isInQueue) {
            val item = lobby.getItem(event.player.inventory.heldItemSlot)
            item.onClick(event)
        } else {
            if (event.player.inventory.heldItemSlot == 8) {
                pp.leaveQueue()
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onEntityInteract(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player) return
        val p = event.damager as Player
        val item = lobby.getItem(p.inventory.heldItemSlot)
        val pp = PracticePlayer.getPlayer(p)
        if (!pp.isInQueue && lobby.world == event.entity.world) {
            item.onClick(event)
        }
    }

    @EventHandler
    fun onInvMove(event: InventoryClickEvent) {
        if (KitEditor.hasInstance() && KitEditor.getInstance().hasPlayer(event.whoClicked as Player)) return
        if (!lobby.allowEditingInventory && lobby.world == event.whoClicked.world) {
            if (event.whoClicked.hasPermission("practice.lobby.bypass") && event.whoClicked.gameMode == GameMode.CREATIVE) return
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onLobbyLeave(event: PlayerQuitEvent) {
        if (lobby.world == event.player.world)
            lobby.removePlayer(PracticePlayer.getPlayer(event.player), true)
    }

    @EventHandler
    fun onLobbyLeave(event: PlayerChangedWorldEvent) {
        if (lobby.world == event.from)
            lobby.removePlayer(PracticePlayer.getPlayer(event.player), true)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onLobbyJoin(event: PlayerChangedWorldEvent) {
        if (lobby.world == event.player.world) {
            val pp = PracticePlayer.getPlayer(event.player)
            lobby.addPlayer(pp)
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onLobbyJoin(event: PlayerJoinEvent) {
        if (lobby.world == event.player.world) {
            val pp = PracticePlayer.getPlayer(event.player)
            lobby.addPlayer(pp)
        }
    }

    @EventHandler
    fun onEntityDamage(event: PlayerDropItemEvent) {
        if (lobby.world == event.player.world && !lobby.canDropItems) event.isCancelled = true
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if (lobby.world == event.entity.world && lobby.lockHunger) event.isCancelled = true
    }

    @EventHandler
    fun onWeatherChange(event: WeatherChangeEvent) {
        if (lobby.world == event.world && lobby.lockWeather) event.isCancelled = true
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.to.y < lobby.voidOutHeight && lobby.world == event.player.world) {
            event.player.teleport(lobby.getSpawn())
        }
        for (l in lobby.launchpads) {
            l.tryLaunch(event.player)
        }
    }
}