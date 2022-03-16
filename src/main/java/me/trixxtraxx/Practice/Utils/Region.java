package me.trixxtraxx.Practice.Utils;

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
        int minx = location1.getBlockX();
        int maxx = location2.getBlockX();
        int miny = location1.getBlockY();
        int maxy = location2.getBlockY();
        int minz = location1.getBlockZ();
        int maxz = location2.getBlockZ();

        if (minx > maxx)
        {
            minx = location2.getBlockX();
            maxx = location1.getBlockX();
        }

        if (miny > maxy)
        {
            miny = location2.getBlockY();
            maxy = location1.getBlockY();
        }

        if (minz > maxz)
        {
            minz = location2.getBlockZ();
            maxz = location1.getBlockZ();
        }

        loc1 = new ConfigLocation(minx, miny, minz);
        loc2 = new ConfigLocation( maxx, maxy, maxz);
    }

    public boolean contains(Location loc)
    {
        if (loc.getBlockX() >= loc1.getLocation(loc.getWorld()).getBlockX() && loc.getBlockY() >= loc1.getLocation(loc.getWorld()).getBlockY() && loc.getBlockZ() >= loc1.getLocation(loc.getWorld()).getBlockZ() && loc.getBlockX() <= loc2.getLocation(loc.getWorld()).getBlockX() && loc.getBlockY() <= loc2.getLocation(loc.getWorld()).getBlockY() && loc.getBlockZ() <= loc2.getLocation(loc.getWorld()).getBlockZ())
        {
            return true;
        }
        return false;
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
}
