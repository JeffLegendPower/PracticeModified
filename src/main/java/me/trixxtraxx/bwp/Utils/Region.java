package me.trixxtraxx.bwp.Utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Region
{
    private Location loc1;
    private Location loc2;

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

        loc1 = new Location(location1.getWorld(), minx, miny, minz);
        loc2 = new Location(location1.getWorld(), maxx, maxy, maxz);
    }

    public boolean contains(Location loc)
    {
        if (loc.getBlockX() >= loc1.getBlockX() && loc.getBlockY() >= loc1.getBlockY() && loc.getBlockZ() >= loc1.getBlockZ() && loc.getBlockX() <= loc2.getBlockX() && loc.getBlockY() <= loc2.getBlockY() && loc.getBlockZ() <= loc2.getBlockZ())
        {
            return true;
        }
        return false;
    }

    public List<Location> getLocations()
    {
        List<Location> locs = new ArrayList<>();
        for (int x = loc1.getBlockX(); x < loc2.getBlockX(); x++)
        {
            for (int y = loc1.getBlockY(); y < loc2.getBlockY(); y++)
            {
                for (int z = loc1.getBlockZ(); z < loc2.getBlockZ(); z++)
                {
                    locs.add(new Location(loc1.getWorld(), x, y, z));
                }
            }
        }
        return locs;
    }
}
