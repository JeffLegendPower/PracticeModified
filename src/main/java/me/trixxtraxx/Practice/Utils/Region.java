package me.trixxtraxx.Practice.Utils;

import me.trixxtraxx.Practice.Practice;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

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

        Practice.log(4, "Region: minX" + minX + " maxX" + maxX + " minY" + minY + " maxY" + maxY + " minZ" + minZ + " maxZ" + maxZ);
        
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
        List<Location> locs = new ArrayList<>();
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
}
