package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
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
        Practice.log(4, "BreakRegion initialized");
        this.region = r;
        this.canBreak = v;
    }

    public BreakRegion(Map map){super(map);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onEvent(BlockBreakEvent e)
    {
        Practice.log(4, "BreakRegion event triggered, 1");
        if (region.contains(e.getBlock().getLocation()))
        {
            Practice.log(4, "BreakRegion takes effect!");
            e.setCancelled(!canBreak);
        }
    }
}