package me.trixxtraxx.Practice.Lobby.ItemTypes

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv
import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv.CloseEvent
import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv.SlotClickEvent
import me.TrixxTraxx.InventoryAPI.Items.BetterItem
import me.TrixxTraxx.Linq.List
import me.TrixxTraxx.Linq.List.Find
import me.TrixxTraxx.StringStorer.StringStorer
import me.trixxtraxx.Practice.Bungee.NewGamePacket
import me.trixxtraxx.Practice.Lobby.LobbyItem
import me.trixxtraxx.Practice.Practice
import me.trixxtraxx.Practice.SQL.PracticePlayer
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class CustomGamemodeItem(
    material: Material?,
    name: String?,
    lore: String?,
    slot: Int,
    private val defaultMode: String,
    private val gamemodes: List<CustomGamemode>) : LobbyItem(material, name, lore, slot) {

    class CustomGamemode(
        var item: BetterItem,
        var gamemode: String,
        var defaultKit: String,
        var solo: Boolean,
        var maps: List<String>
    )

    inner class Challenge(
        var challenger: Player,
        var challenged: Player,
        var gamemode: String,
        var map: String,
        var kit: String
    ) {
        fun start() {
            NewGamePacket.start(List(challenged, challenger), gamemode, map, kit, true)
        }
    }

    private val challenges = HashMap<Player, List<Challenge>>()
    private val defaultKit: String? = null

    override fun onClick(interact: PlayerInteractEvent) {
        if (interact.action == Action.RIGHT_CLICK_AIR || interact.action == Action.RIGHT_CLICK_BLOCK) {
            val p = interact.player
            val inv = BetterInv(p, 54, "Custom Gamemode")
            setInv(inv)
            inv.open()
        }
    }

    override fun onClick(interact: EntityDamageByEntityEvent) {
        Practice.log(4, interact.damager.name + " clicked at entity " + interact.entity.name)
        if (interact.damager is Player && interact.entity is Player) {
            val challenger = interact.damager as Player
            val challenged = interact.entity as Player
            var challanges1 = challenges[challenger]
            if (challanges1 == null) challanges1 = List()
            val chal =
                challanges1.find(Find { x: Challenge -> x.challenger === challenged && x.challenged === challenger })
            if (chal == null) {
                val sql = StringStorer.getPlayer(challenger)
                val gm = getGM(challenger)
                if (gm!!.solo) {
                    interact.damager.sendMessage("§cPlease select a non solo Gamemode!")
                }
                var k: String
                val useCustomKit =
                    java.lang.Boolean.parseBoolean(sql.getOrStoreDefault("Practice_Challenge_CustomKit", "false"))
                k = if (useCustomKit) {
                    challenger.name
                } else {
                    gm.defaultKit
                }
                var m = sql.getOrStoreDefault("Practice_Challenge_CustomMap", "Random")
                if (m.equals("Random", ignoreCase = true)) m = gm.maps.random()
                val g = Challenge(challenger, challenged, gm.gamemode, m, k)
                var chals = challenges[challenged]
                if (chals == null) chals = List()
                chals.removeAll(Find { x: Challenge -> x.challenger.name.equals(challenger.name, ignoreCase = true) })
                chals.add(g)
                challenges[challenged] = chals
                object : BukkitRunnable() {
                    override fun run() {
                        var chals = challenges[challenged]
                        if (chals == null) chals = List()
                        chals!!.remove(g)
                        challenges[challenged] = chals!!
                    }
                }.runTaskLater(Practice.Instance, (20 * 60).toLong())
                challenger.sendMessage("§9Challenged " + challenged.name + " to a Game of " + gm.item.displayName)
                challenged.sendMessage("§9You have been challenged by " + challenger.name + " to a Game of " + gm.item.displayName)
            } else {
                NewGamePacket.start(List(challenged, challenger), chal.gamemode, chal.kit, chal.map, true)
                challenges.remove(challenger)
                challenges.remove(challenged)
            }
        }
    }

    private fun setInv(inv: BetterInv) {
        setCustomKit(inv)
        setCustomMap(inv)
        setCustomGm(inv)
    }

    private fun setMapInv(inv: BetterInv) {
        inv.setItem(10, BetterItem(Material.BARRIER).setDisplayName("§9Random Map"))
        inv.onClickSlot(10) {
            inv.clear()
            StringStorer.getPlayer(inv.player).storeValue("Practice_Challenge_CustomMap", "Random")
            setInv(inv)
        }
        val g = getGM(inv.player)
        if (g == null) {
            Practice.log(1, "No Gamemode found!")
            return
        }
        val maps = g.maps
        //set an MAP Item for each map, the Item should be named after the map and it should start at slot 10 and skip 2 slots every 7 slots
        for (i in maps.indices) {
            var slot = 11 + i
            if (slot > 16) slot += 2
            if (slot > 25) slot += 2
            if (slot > 34) slot += 2
            inv.setItem(
                slot,
                BetterItem(Material.MAP).setDisplayName("§b" + maps[i]).setLore("§9Click to select this map!")
            )
            inv.onClickSlot(slot) { event: SlotClickEvent ->
                event.isCancelled = true
                inv.clear()
                StringStorer.getPlayer(inv.player).storeValue("Practice_Challenge_CustomMap", maps[i])
                setInv(inv)
            }
        }
    }

    private fun setCustomGMInv(inv: BetterInv) {
        var slot = 10
        for (g in gamemodes) {
            Practice.log(4, "Adding Custom Gamemode: " + g!!.gamemode)
            inv.setItem(slot, g.item)
            inv.onClickSlot(slot) { event: SlotClickEvent ->
                event.isCancelled = true
                inv.clear()
                StringStorer.getPlayer(inv.player).storeValue("Practice_Challenge_CustomGamemode", g.gamemode)
                StringStorer.getPlayer(inv.player).storeValue("Practice_Challenge_CustomMap", "Random")
                setInv(inv)
            }
            slot++
            if (slot == 17) slot += 2
            if (slot == 26) slot += 2
            if (slot == 35) slot += 2
        }
    }

    private fun setCustomGm(inv: BetterInv) {
        inv.setItem(
            10,
            BetterItem(Material.BOOK).setDisplayName("§bSelect Gamemode")
                .setLore("§9You can set the Gamemode \n§9you want to play here")
        )
        inv.lock(10)
        val g = getGM(inv.player)
        if (g == null) {
            Practice.log(1, "No Gamemode found!")
            return
        }
        inv.setItem(19, g.item)
        inv.onClickSlot(19) { x: SlotClickEvent ->
            x.isCancelled = true
            inv.clear()
            setCustomGMInv(inv)
        }
        inv.clearOnClose()
        if (g.solo) {
            inv.onClose {
                val pp = PracticePlayer.getPlayer(inv.player)
                //get kit
                val useCustomKit = java.lang.Boolean.parseBoolean(
                    StringStorer.getPlayer(inv.player).getOrStoreDefault("Practice_Challenge_CustomKit", "false")
                )
                //get map
                val map = StringStorer.getPlayer(inv.player).getOrStoreDefault("Practice_Challenge_CustomMap", "Random")
                //start game, if not custom kit then use default kit
                NewGamePacket.start(
                    List(inv.player),
                    g.gamemode,
                    if (useCustomKit) pp.kit.name else g.defaultKit,
                    if (map.equals("Random", ignoreCase = true)) g.maps.random() else map,
                    true
                )
            }
        }
    }

    fun getGM(p: Player?): CustomGamemode? {
        val gm = StringStorer.getPlayer(p).getOrStoreDefault("Practice_Challenge_CustomGamemode", defaultMode)
        var g = gamemodes.find { x: CustomGamemode? -> x!!.gamemode.equals(gm, ignoreCase = true) }
        if (g == null) g = gamemodes.find { x: CustomGamemode? -> x!!.gamemode.equals(defaultMode, ignoreCase = true) }
        if (g == null) g = gamemodes.first()
        return g
    }

    private fun setCustomMap(inv: BetterInv) {
        inv.setItem(13, BetterItem(Material.BOOK).setDisplayName("§bSet a Map").setLore("§9Use the map of your choice"))
        inv.lock(13)
        val customMap = StringStorer.getPlayer(inv.player).getOrStoreDefault("Practice_Challenge_CustomMap", "Random")
        inv.setItem(22, BetterItem(Material.MAP).setDisplayName("§9Map: §b$customMap"))
        inv.onClickSlot(22) { x: SlotClickEvent ->
            x.isCancelled = true
            inv.clear()
            setMapInv(inv)
        }
    }

    private fun setCustomKit(inv: BetterInv) {
        inv.setItem(
            16,
            BetterItem(Material.BOOK).setDisplayName("§bToggle Custom Kit")
                .setLore("§9Use the kit you made in the Kit Editing Area")
        )
        inv.lock(16)
        val customKit = java.lang.Boolean.parseBoolean(
            StringStorer.getPlayer(inv.player).getOrStoreDefault("Practice_Challenge_CustomKit", "false")
        )
        if (customKit) inv.setItem(
            25,
            BetterItem(Material.INK_SACK).NsetDurability(10.toShort()).setDisplayName("§9Custom Kit: §bEnabled")
        ) else inv.setItem(
            25, BetterItem(
                Material.INK_SACK
            ).NsetDurability(8.toShort()).setDisplayName("§9Custom Kit: §bDisabled")
        )
        inv.onClickSlot(25) { x: SlotClickEvent ->
            x.isCancelled = true
            inv.clear()
            StringStorer.getPlayer(inv.player).storeValue("Practice_Challenge_CustomKit", customKit.toString() + "")
            setInv(inv)
        }
    }
}