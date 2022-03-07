package me.trixxtraxx.bwp.Map.Components;

import me.trixxtraxx.bwp.GameEvents.GameEvent;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.bwp.Map.Map;
import me.trixxtraxx.bwp.Map.MapComponent;
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

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof BlockPlaceEvent) onBlockPlace((BlockPlaceEvent) event);
        if(event instanceof BlockBreakEvent) onBlockBreak((BlockBreakEvent) event);
    }

    @Override
    public void onEventAfter(Event event)
    {
        if(event instanceof BlockPlaceEvent) onBlockPlace2((BlockPlaceEvent) event);
    }

    @Override
    public void onEvent(GameEvent event)
    {
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
    }

    public void onBlockPlace(BlockPlaceEvent e)
    {
        blocksPlaced.add(e.getBlock().getLocation());
    }
    public void onBlockPlace2(BlockPlaceEvent e)
    {
        if(e.isCancelled()) blocksPlaced.remove(e.getBlock().getLocation());
    }

    public void onBlockBreak(BlockBreakEvent e)
    {
        if(!blocksPlaced.contains(e.getBlock().getLocation())) e.setCancelled(true);
    }

    public void onReset(ResetEvent e)
    {
        blocksPlaced.clear();
    }
}
