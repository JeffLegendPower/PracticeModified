package me.trixxtraxx.Practice.Lobby

import me.trixxtraxx.Practice.Practice
import me.trixxtraxx.Practice.Utils.ConfigLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class Launchpad(location: ConfigLocation, private var radius: Double, velocity: String, w: World?) {
    private var loc: Location
    private var velocity: Vector
    private val cooldown = me.TrixxTraxx.Linq.List<Player>()

    init {
        loc = location.getLocation(w)
        val velo = velocity
        val split = velo.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.velocity = Vector(split[0].toDouble(), split[1].toDouble(), split[2].toDouble())
    }

    fun tryLaunch(p: Player) {
        if (p.location.distance(loc) > radius) return
        if (cooldown.contains(p)) return
        launch(p)
    }

    fun launch(p: Player) {
        cooldown.add(p)
        Bukkit.getScheduler().runTaskLater(Practice.Instance, { cooldown.remove(p) }, 20)
        p.velocity = velocity
    }
}