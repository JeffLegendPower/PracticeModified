package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BedLayerComponent extends GameComponent
{
    private Material mat;
    private Location loc1;
    private Location loc2;
    private int layer;
    private boolean butterfly;

    public BedLayerComponent(GameLogic logic, Material mat, Location loc1, Location loc2, int layer, boolean butterfly)
    {
        super(logic);
        this.mat = mat;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.layer = layer;
        this.butterfly = butterfly;
    }

    @Override
    public void onEvent(GameEvent event)
    {
        if(event instanceof StartEvent) onStart((StartEvent) event);
    }

    public void onStart(StartEvent e)
    {
        List<Block> blocks = getDefenceBlocks();
        for (Block b : blocks)
        {
            b.setType(mat);
        }
    }

    public List<Block> getDefenceBlocks()
    {
        Block b1 = loc1.getBlock();
        Block b2 = loc2.getBlock();
        BlockFace f = b1.getFace(b2);
        BlockFace f0 = f.getOppositeFace();
        BlockFace f1 = getNextFace(f0);
        BlockFace f2 = f1.getOppositeFace();

        List<Block> blocks = getBlocks(b1, new BlockFace[]{f0, f1, BlockFace.UP});
        blocks.addAll(getBlocks(b1, new BlockFace[]{f0, f2, BlockFace.UP}));
        blocks.addAll(getBlocks(b2, new BlockFace[]{f, f2, BlockFace.UP}));
        blocks.addAll(getBlocks(b2, new BlockFace[]{f, f2, BlockFace.UP}));
        return blocks;
    }

    private List<Block> getBlocks(Block b, BlockFace[] faces)
    {
        List<Block> blocks = new ArrayList<>();
        for(int i1 = 0; i1 <= layer; i1++)
        {
            for (int i2 = 0; i2 <= layer - i1; i2++)
            {
                Block block = b;
                int i3 = layer - i1 - i2;
                for (int i = 0; i < i1; i++) block = block.getRelative(faces[0]);
                for (int i = 0; i < i2; i++) block = block.getRelative(faces[1]);
                for (int i = 0; i < i3; i++) block = block.getRelative(faces[2]);
            }
        }
        return blocks;
    }

    private BlockFace getNextFace(BlockFace face)
    {
        if(face == BlockFace.SOUTH || face == BlockFace.NORTH) return BlockFace.EAST;
        else return BlockFace.SOUTH;
    }
}
