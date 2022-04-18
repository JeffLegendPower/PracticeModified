package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;

public class NoMapBreakComponent extends MapComponent
{
    
    public NoMapBreakComponent(Map map)
    {
        super(map);
        Practice.log(4, "NoMapBreakComponent initialized");
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onEvent(BlockBreakEvent e)
    {
        e.setCancelled(true);
    }
}
