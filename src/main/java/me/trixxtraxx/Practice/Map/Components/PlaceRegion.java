package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceRegion extends MapComponent
{
    @Config
    public Region region;
    @Config
    public boolean canPlace;

    public PlaceRegion(Map map, Region r, boolean canPlace)
    {
        super(map);
        region = r;
        this.canPlace = canPlace;
    }

    public PlaceRegion(Map map){super(map);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(BlockPlaceEvent e)
    {
        if(region.contains(e.getBlock().getLocation()))
        {
            e.setCancelled(!canPlace);
        }
    }
}
