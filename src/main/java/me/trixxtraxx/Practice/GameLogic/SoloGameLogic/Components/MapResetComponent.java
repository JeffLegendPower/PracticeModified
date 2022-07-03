package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;

import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class MapResetComponent extends GameComponent
{
    public List<BlockStorage> blocks = new List<>();
    public List<Location> liquidBlocks = new List<>();
    
    public static class BlockStorage
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
    
    @TriggerEvent(priority = 10, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onExplode(EntityExplodeEvent e)
    {
        for (Block block : e.blockList())
        {
            blocks.add(new BlockStorage()
            {{
                loc = block.getLocation();
                b = block.getData();
                mat = block.getType();
            }});
        }
    }
    
    @TriggerEvent(priority = 10, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onExplode(BlockExplodeEvent e)
    {
        for (Block block : e.blockList())
        {
            blocks.add(new BlockStorage()
            {{
                loc = block.getLocation();
                b = block.getData();
                mat = block.getType();
            }});
        }
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onBucketEmpty(PlayerBucketEmptyEvent event){
        liquidBlocks.add(event.getBlockClicked().getLocation());
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    @SuppressWarnings("deprecation")
    public void onResetAfter(ResetEvent e)
    {
        blocks.reverse();
        for (BlockStorage store:blocks)
        {
            store.loc.getBlock().setType(store.mat);
            store.loc.getBlock().setData(store.b);
        }
        List<BlockFace> faces = new List<>(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
        for(Location loc:liquidBlocks){
            loc.getBlock().setType(Material.AIR);
            List<Location> checked = new List<>();
            List<Location> current = new List<>(loc);
            
            while(true)
            {
                if(current.isEmpty()) break;
                List<Location> next = new List<>();
                for(Location l: current){
                    for(BlockFace face: faces){
                        Location n = l.getBlock().getRelative(face).getLocation();
                        if(checked.contains(n)) continue;
                        checked.add(n);
                        if(n.getBlock().isLiquid()) next.add(n);
                    }
                }
                next.forEach(x -> x.getBlock().setType(Material.AIR));
                current = next;
            }
        }
        blocks.clear();
        liquidBlocks.clear();
    }
}
