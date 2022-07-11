package me.trixxtraxx.Practice.Lobby;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Launchpad
{
    private Location loc;
    private double radius;
    private Vector velocity;

    private List<Player> cooldown = new List<>();

    public Launchpad(ConfigLocation location, double radius, String velocity, World w) {
        loc = location.getLocation(w);
        this.radius = radius;
        String[] split = velocity.split(",");
        this.velocity = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }

    public void tryLaunch(Player p)
    {
        if(p.getLocation().distance(loc) > radius) return;
        if(cooldown.contains(p)) return;
        launch(p);
    }

    public void launch(Player p){
        cooldown.add(p);
        Bukkit.getScheduler().runTaskLater(Practice.Instance, () -> cooldown.remove(p), 20);
        p.setVelocity(velocity);
    }
}