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
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;
import org.bukkit.event.entity.EntityExplodeEvent;

public class NoMapBreakComponent extends MapComponent
{
    private List<Block> blocks = new List<>();
    
    public NoMapBreakComponent(Map map)
    {
        super(map);
        Practice.log(4, "NoMapBreakComponent initialized");
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onEvent(BlockBreakEvent e)
    {
        if(!blocks.contains(e.getBlock())) e.setCancelled(true);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.NONE)
    public void onEvent(EntityExplodeEvent e)
    {
        Practice.log(4, "EntityExplodeEvent");
        List<Block> toRemove = new List<>();
        for(Block block : e.blockList())
        {
            if(!blocks.contains(block)) toRemove.add(block);
        }
        Practice.log(4, "Now Removing " + toRemove.size() + " blocks");
        for(Block block : toRemove)
        {
            e.blockList().remove(block);
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.NONE)
    public void onEvent(BlockExplodeEvent e)
    {
        Practice.log(4, "BlockExplodeEvent");
        List<Block> toRemove = new List<>();
        for(Block block: e.blockList())
        {
            if(!blocks.contains(block)) toRemove.add(block);
        }
        Practice.log(4, "Now Removing " + toRemove.size() + " blocks");
        for(Block block: toRemove)
        {
            e.blockList().remove(block);
        }
    }
    
    
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(BlockPlaceEvent e)
    {
        blocks.add(e.getBlock());
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(ResetEvent e)
    {
        blocks.clear();
    }
}
