package me.trixxtraxx.Practice.Lobby

import me.TrixxTraxx.InventoryAPI.Items.BetterItem
import me.TrixxTraxx.Linq.List
import me.trixxtraxx.Practice.Bungee.BungeeUtil
import me.trixxtraxx.Practice.Practice
import me.trixxtraxx.Practice.SQL.PracticePlayer
import me.trixxtraxx.Practice.Utils.ConfigLocation
import me.trixxtraxx.Practice.libs.Fastboard.FastBoard
import org.bukkit.*
import org.bukkit.potion.PotionEffect
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class Lobby(val world: World,
            val name: String,
            val maxRatedPlayers: Int,
            val canPlaceBlocks: Boolean,
            val canBreakBlocks: Boolean,
            val canDamageBlocks: Boolean,
            val allowEntityDamage: Boolean,
            val allowEditingInventory: Boolean,
            val canDropItems: Boolean,
            val lockHunger: Boolean,
            val lockWeather: Boolean,
            val voidOutHeight: Int,
            val blockedInventories: List<String>,
            val items: List<LobbyItem>,
            val launchpads: List<Launchpad>,
            val spawn: ConfigLocation) {

    val players = List<PracticePlayer>()
    private val scoreboards = List<FastBoard>()

    init {
        lobbies.add(this)
    }

    fun getSpawn(): Location {
        return spawn.getLocation(world)
    }

    fun getItem(slot: Int): LobbyItem {
        return items.find { x: LobbyItem -> x.slot == slot }
    }

    fun addPlayer(player: PracticePlayer?) {
        if (player == null || player.player == null || players.contains(player)) return
        Practice.log(4, "Adding player " + player.name + " to lobby " + name)
        players.add(player)
        object : BukkitRunnable() {
            override fun run() {
                val p = player.player
                if (p == null) {
                    //had an exception so idk...
                    players.remove(player)
                    return
                }
                p.maxHealth = 20.0
                p.health = 20.0
                p.foodLevel = 20
                p.allowFlight = false
                p.fireTicks = 0
                p.noDamageTicks = 20
                p.gameMode = GameMode.SURVIVAL
                p.maximumNoDamageTicks = 20
                p.teleport(spawn.getLocation(world))
                p.activePotionEffects.forEach(Consumer { x: PotionEffect -> p.removePotionEffect(x.type) })
                setInv(player)
                val board = FastBoard(player.player)
                board.updateTitle("§bPractice")
                board.updateLines(
                    "",
                    "§9Player: §b" + player.name,
                    "",
                    "§bRanked.fun"
                )
            }
        }.runTaskLater(Practice.Instance, 1)
        BungeeUtil.getInstance().update()
    }

    fun removePlayer(player: PracticePlayer, update: Boolean) {
        scoreboards.removeAll { x: FastBoard ->
            if (x.player === player) {
                x.delete()
                return@removeAll true
            }
            println("hi")
            false
        }
        if (players.remove(player)) if (update) BungeeUtil.getInstance().update()
    }

    fun setInv(player: PracticePlayer) {
        val p = player.player
        val inv = p.inventory
        inv.clear()
        if (!player.isInQueue) {
            inv.armorContents = null
            for (item in items) {
                inv.setItem(item.slot, item.item)
            }
        } else {
            p.inventory.setItem(
                8,
                BetterItem(Material.BARRIER).setDisplayName(ChatColor.RED.toString() + "Leave Queue")
            )
        }
    }

    companion object {
        @JvmStatic
        val lobbies = List<Lobby>()
    }
}