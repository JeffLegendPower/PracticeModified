package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class NoMapBreakComponent extends MapComponent
{
    private List<Location> blocksPlaced = new ArrayList<>();

    public NoMapBreakComponent(Map map)
    {
        super(map);
    }

    public void onEvent(BlockPlaceEvent e)
    {
        blocksPlaced.add(e.getBlock().getLocation());
    }
    public void onEventCancel(BlockPlaceEvent e)
    {
        if(e.isCancelled()) blocksPlaced.remove(e.getBlock().getLocation());
    }

    public void onEvent(BlockBreakEvent e)
    {
        if(!blocksPlaced.contains(e.getBlock().getLocation())) e.setCancelled(true);
        else blocksPlaced.remove(e.getBlock().getLocation());
    }

    public void onEvent(ResetEvent e)
    {
        blocksPlaced.clear();
    }
}
