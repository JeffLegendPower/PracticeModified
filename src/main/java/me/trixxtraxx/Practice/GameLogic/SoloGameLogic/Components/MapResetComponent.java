package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;

import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class MapResetComponent extends GameComponent
{
    private List<BlockStorage> blocks = new ArrayList<>();

    private class BlockStorage
    {
        public Location loc;
        public Material mat;
        public byte b;
    }

    public MapResetComponent(GameLogic logic)
    {
        super(logic);
    }
    
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    @SuppressWarnings("deprecation")
    public void onBlockPlace(BlockPlaceEvent e)
    {
        for (BlockStorage b:blocks)
        {
            if(b.loc.distance(e.getBlock().getLocation()) < 1) return;
        }
        BlockStorage store = new BlockStorage();
        store.loc = e.getBlock().getLocation();
        store.mat = Material.AIR;
        blocks.add(store);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    @SuppressWarnings("deprecation")
    public void onBlockBreak(BlockBreakEvent e)
    {
        for (BlockStorage b:blocks)
        {
            if(b.loc.distance(e.getBlock().getLocation()) < 1) return;
        }
        BlockStorage store = new BlockStorage();
        store.loc = e.getBlock().getLocation();
        store.b = e.getBlock().getData();
        store.mat = e.getBlock().getType();
        blocks.add(store);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    @SuppressWarnings("deprecation")
    public void onResetAfter(ResetEvent e)
    {
        for (BlockStorage store:blocks)
        {
            store.loc.getBlock().setType(store.mat);
            store.loc.getBlock().setData(store.b);
        }
        blocks.clear();
    }
}
