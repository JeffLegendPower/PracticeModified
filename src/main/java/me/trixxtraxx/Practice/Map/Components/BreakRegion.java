package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakRegion extends MapComponent
{
    private Settings settings = new Settings();

    private class Settings
    {
        public Region region;
        public boolean canBreak;
    }

    public BreakRegion(Map map, Region r, boolean v)
    {
        super(map);
        settings.region = r;
        settings.canBreak = v;
    }

    public BreakRegion(Map map, String data)
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
        if (event instanceof BlockBreakEvent) onBreak((BlockBreakEvent) event);
    }

    public void onBreak(BlockBreakEvent e)
    {
        if (settings.region.contains(e.getBlock().getLocation()))
        {
            e.setCancelled(settings.canBreak);
        }
    }
}
