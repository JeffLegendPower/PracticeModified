package me.trixxtraxx.bwp.Map.Components;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.Map.Map;
import me.trixxtraxx.bwp.Map.MapComponent;
import me.trixxtraxx.bwp.Utils.Region;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceRegion extends MapComponent
{
    private Region region;
    private boolean canPlace;

    public PlaceRegion(Map map, Region r, boolean canPlace)
    {
        super(map);
        region = r;
        this.canPlace = canPlace;
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof BlockPlaceEvent) onPlace((BlockPlaceEvent) event);
    }

    public void onPlace(BlockPlaceEvent e)
    {
        if(region.contains(e.getBlock().getLocation()))
        {
            e.setCancelled(!canPlace);
        }
    }
}
