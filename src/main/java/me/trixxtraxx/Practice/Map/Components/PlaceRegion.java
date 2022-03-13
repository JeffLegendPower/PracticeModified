package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceRegion extends MapComponent
{
    private Settings settings = new Settings();

    private class Settings
    {
        public Region region;
        public boolean canPlace;
    }

    public PlaceRegion(Map map, Region r, boolean canPlace)
    {
        super(map);
        settings.region = r;
        settings.canPlace = canPlace;
    }

    public PlaceRegion(Map map, String data)
    {
        super(map);
        settings = new Gson().fromJson(data, Settings.class);
    }

    @Override
    public String getData()
    {
        return new Gson().toJson(settings);
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof BlockPlaceEvent) onPlace((BlockPlaceEvent) event);
    }

    public void onPlace(BlockPlaceEvent e)
    {
        if(settings.region.contains(e.getBlock().getLocation()))
        {
            e.setCancelled(!settings.canPlace);
        }
    }
}
