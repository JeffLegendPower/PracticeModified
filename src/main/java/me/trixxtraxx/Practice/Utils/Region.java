package me.trixxtraxx.Practice.Utils;

import me.trixxtraxx.Practice.Practice;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import me.TrixxTraxx.Linq.List;
import java.util.Map;

public class Region
{
    private ConfigLocation loc1;
    private ConfigLocation loc2;

    public Region(Location location1, Location location2)
    {
        //get min and max of x y z of both locations
        int minX = Math.min(location1.getBlockX(), location2.getBlockX());
        int maxX = Math.max(location1.getBlockX(), location2.getBlockX());
        int minY = Math.min(location1.getBlockY(), location2.getBlockY());
        int maxY = Math.max(location1.getBlockY(), location2.getBlockY());
        int minZ = Math.min(location1.getBlockZ(), location2.getBlockZ());
        int maxZ = Math.max(location1.getBlockZ(), location2.getBlockZ());
        
        //set locations
        loc1 = new ConfigLocation(minX, minY, minZ);
        loc2 = new ConfigLocation(maxX, maxY, maxZ);
    }
    
    public Region(ConfigLocation location1, ConfigLocation location2)
    {
        //get min and max of x y z of both locations
        double minX = Math.min(location1.getX(), location2.getX());
        double maxX = Math.max(location1.getX(), location2.getX());
        double minY = Math.min(location1.getY(), location2.getY());
        double maxY = Math.max(location1.getY(), location2.getY());
        double minZ = Math.min(location1.getZ(), location2.getZ());
        double maxZ = Math.max(location1.getZ(), location2.getZ());
    
        //set locations
        loc1 = new ConfigLocation(minX, minY, minZ);
        loc2 = new ConfigLocation(maxX, maxY, maxZ);
    }

    public boolean contains(Location loc)
    {
        //check if loc is contain within the 2 locations
        return (loc.getBlockX() >= loc1.getLocation(loc.getWorld()).getBlockX() && loc.getBlockX() <= loc2.getLocation(loc.getWorld()).getBlockX()) &&
                (loc.getBlockY() >= loc1.getLocation(loc.getWorld()).getBlockY() && loc.getBlockY() <= loc2.getLocation(loc.getWorld()).getBlockY()) &&
                (loc.getBlockZ() >= loc1.getLocation(loc.getWorld()).getBlockZ() && loc.getBlockZ() <= loc2.getLocation(loc.getWorld()).getBlockZ());
    }

    public List<Location> getLocations(World w)
    {
        List<Location> locs = new List<>();
        for (int x = loc1.getLocation(w).getBlockX(); x < loc2.getLocation(w).getBlockX(); x++)
        {
            for (int y = loc1.getLocation(w).getBlockY(); y < loc2.getLocation(w).getBlockY(); y++)
            {
                for (int z = loc1.getLocation(w).getBlockZ(); z < loc2.getLocation(w).getBlockZ(); z++)
                {
                    locs.add(new Location(w, x, y, z));
                }
            }
        }
        return locs;
    }
    
    public Location getLocation1(World w)
    {
        return loc1.getLocation(w);
    }
    
    public Location getLocation2(World w)
    {
        return loc2.getLocation(w);
    }
    
    public String serialize()
    {
        return loc1.serialize() + ";" + loc2.serialize();
    }
    public static Region deserialize(String s)
    {
        String[] locs = s.split(";");
        return new Region(ConfigLocation.deserialize(locs[0]), ConfigLocation.deserialize(locs[1]));
    }
    
    @Override
    public String toString(){
        return serialize();
    }
}