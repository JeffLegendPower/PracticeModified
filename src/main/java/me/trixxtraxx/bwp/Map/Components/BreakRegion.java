package me.trixxtraxx.bwp.Map.Components;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.Map.Map;
import me.trixxtraxx.bwp.Map.MapComponent;
import me.trixxtraxx.bwp.Utils.Region;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BreakRegion extends MapComponent
{
    private Region region;
    private boolean canBreak;

    public BreakRegion(Map map, Region r, boolean v)
    {
        super(map);
        region = r;
        this.canBreak = canBreak;
    }

    @Override
    public void onEvent(Event event)
    {
        if (event instanceof BlockBreakEvent) onBreak((BlockBreakEvent) event);
    }

    public void onBreak(BlockBreakEvent e)
    {
        if (region.contains(e.getBlock().getLocation()))
        {
            e.setCancelled(canBreak);
        }
    }
}
