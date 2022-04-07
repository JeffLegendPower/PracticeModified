package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakRegion extends MapComponent
{
    @Config
    public Region region;
    @Config
    public boolean canBreak;

    public BreakRegion(Map map, Region r, boolean v)
    {
        super(map);
        region = r;
        canBreak = v;
    }

    public BreakRegion(Map map){super(map);}

    @Override
    public void onEvent(Event event)
    {
        if (event instanceof BlockBreakEvent) onBreak((BlockBreakEvent) event);
    }

    public void onBreak(BlockBreakEvent e)
    {
        if (region.contains(e.getBlock().getLocation()))
        {
            e.setCancelled(!canBreak);
        }
    }
}
