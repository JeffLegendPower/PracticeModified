package me.trixxtraxx.Practice.Map.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class HeightLimitComponent extends MapComponent
{
    @Config
    public int heightLimit;
    
    public HeightLimitComponent(Map map)
    {
        super(map);
    }
    
    public HeightLimitComponent(Map map, int heightLimit)
    {
        super(map);
        this.heightLimit = heightLimit;
    }
    
    @TriggerEvent
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(event.getBlockPlaced().getY() >= heightLimit)
        {
            event.setCancelled(true);
        }
    }
}
