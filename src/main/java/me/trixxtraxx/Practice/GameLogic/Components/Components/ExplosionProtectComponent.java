package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class ExplosionProtectComponent extends GameComponent
{
    @Config
    public Material mat;
    
    public ExplosionProtectComponent(GameLogic logic)
    {
        super(logic);
    }
    
    public ExplosionProtectComponent(GameLogic logic, Material mat)
    {
        super(logic);
        this.mat = mat;
    }
    
    @TriggerEvent
    public void onFb(EntityExplodeEvent e)
    {
        List<Block> blocks = new ArrayList<Block>();
        for (Block b : e.blockList())
        {
            if(b.getType() == mat)
            {
                blocks.add(b);
            }
        }
        e.blockList().removeAll(blocks);
    }
    
    @TriggerEvent
    public void onTnt(BlockExplodeEvent e)
    {
        List<Block> blocks = new ArrayList<Block>();
        for (Block b : e.blockList())
        {
            if(b.getType() == mat)
            {
                blocks.add(b);
            }
        }
        for (Block b : e.blockList())
        {
            if(blocks.contains(b)) continue;
            int distance = (int)b.getLocation().distance(e.getBlock().getLocation());
            if(distance != 0)
            {
                BlockIterator iterator = new BlockIterator(b.getWorld(), b.getLocation().toVector(), b.getLocation().setDirection(e.getBlock().getLocation().toVector()).getDirection(), 0, distance);
                while (iterator.hasNext())
                {
                    Block block = iterator.next();
                    //Bedwars.LogInformation(4, "Iterator Location: " + block.getLocation().toString());
                    for(Block b2 : blocks)
                    {
                        Location loc1 = b2.getLocation();
                        Location loc2 = block.getLocation();
                        if(loc1.getBlockX() == loc2.getBlockX() &&
                                loc1.getBlockY() == loc2.getBlockY() &&
                                loc1.getBlockZ() == loc2.getBlockZ())
                        {
                            blocks.add(b);
                            break;
                        }
                    }
                }
            }
        }
        e.blockList().removeAll(blocks);
    }
    
    @TriggerEvent
    public void onTnt(EntityExplodeEvent e)
    {
        List<Block> blocks = new ArrayList<Block>();
        for (Block b : e.blockList())
        {
            if(b.getType() == mat)
            {
                blocks.add(b);
            }
        }
        for (Block b : e.blockList())
        {
            if(blocks.contains(b)) continue;
            int distance = (int)b.getLocation().distance(e.getLocation());
            if(distance != 0)
            {
                BlockIterator iterator = new BlockIterator(b.getWorld(), b.getLocation().toVector(), b.getLocation().setDirection(e.getLocation().toVector()).getDirection(), 0, distance);
                while (iterator.hasNext())
                {
                    Block block = iterator.next();
                    //Bedwars.LogInformation(4, "Iterator Location: " + block.getLocation().toString());
                    for(Block b2 : blocks)
                    {
                        Location loc1 = b2.getLocation();
                        Location loc2 = block.getLocation();
                        if(loc1.getBlockX() == loc2.getBlockX() &&
                                loc1.getBlockY() == loc2.getBlockY() &&
                                loc1.getBlockZ() == loc2.getBlockZ())
                        {
                            blocks.add(b);
                            break;
                        }
                    }
                }
            }
        }
        e.blockList().removeAll(blocks);
    }
}
