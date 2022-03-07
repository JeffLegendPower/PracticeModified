package me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.bwp.GameEvents.GameEvent;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;

import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Events.ResetEvent;
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

    @Override
    public void onEventAfter(Event event)
    {
        if(event instanceof BlockPlaceEvent) onBlockPlaceAfter((BlockPlaceEvent) event);
        if(event instanceof BlockBreakEvent) onBlockBreakAfter((BlockBreakEvent) event);
    }

    @Override
    public void onEventAfter(GameEvent event)
    {
        if(event instanceof ResetEvent) onResetAfter((ResetEvent) event);
    }

    @SuppressWarnings("deprecation")
    public void onBlockPlaceAfter(BlockPlaceEvent e)
    {
        if(e.isCancelled()) return;
        for (BlockStorage b:blocks)
        {
            if(b.loc.distance(e.getBlock().getLocation()) < 1) return;
        }
        BlockStorage store = new BlockStorage();
        store.loc = e.getBlock().getLocation();
        store.mat = Material.AIR;
        blocks.add(store);
    }

    @SuppressWarnings("deprecation")
    public void onBlockBreakAfter(BlockBreakEvent e)
    {
        if(e.isCancelled()) return;
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

    @SuppressWarnings("deprecation")
    public void onResetAfter(ResetEvent e)
    {
        if(e.isCanceled()) return;
        for (BlockStorage store:blocks)
        {
            store.loc.getBlock().setType(store.mat);
            store.loc.getBlock().setData(store.b);
        }
        blocks.clear();
    }
}
