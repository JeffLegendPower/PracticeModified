package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.ScoreboardComponent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings.ISettingsComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BedLayerComponent extends MapComponent implements ISettingsComponent
{
    private Settings settings = new Settings();
    private class Settings
    {
        private Material mat;
        private ConfigLocation loc1;
        private ConfigLocation loc2;
        private int layer;
        private boolean butterfly;
    }

    public BedLayerComponent(Map m, Material mat, ConfigLocation loc1, ConfigLocation loc2, int layer, boolean butterfly)
    {
        super(m);
        settings.mat = mat;
        settings.loc1 = loc1;
        settings.loc2 = loc2;
        settings.layer = layer;
        settings.butterfly = butterfly;
    }
    public BedLayerComponent(Map m, String s)
    {
        super(m);
        settings = new Gson().fromJson(s, Settings.class);
    }
    @Override
    public String getData() {return new Gson().toJson(settings);}

    @Override
    public void onEvent(GameEvent event)
    {
        if(event instanceof StartEvent) onStart((StartEvent) event);
    }

    public void onStart(StartEvent e)
    {
        List<Block> blocks = getDefenceBlocks();
        Practice.log(4, "Now placing: " + blocks.size() + " blocks");
        for (Block b : blocks)
        {
            if(b.getType() == Material.AIR) b.setType(settings.mat);
        }
    }

    public List<Block> getDefenceBlocks()
    {
        Block b1 = settings.loc1.getLocation(map.getWorld()).getBlock();
        Block b2 = settings.loc2.getLocation(map.getWorld()).getBlock();
        BlockFace f = b1.getFace(b2);
        BlockFace f0 = f.getOppositeFace();
        BlockFace f1 = getNextFace(f0);
        BlockFace f2 = f1.getOppositeFace();

        List<Block> blocks = getBlocks(b1, new BlockFace[]{f0, f1, BlockFace.UP});
        blocks.addAll(getBlocks(b1, new BlockFace[]{f0, f2, BlockFace.UP}));
        blocks.addAll(getBlocks(b2, new BlockFace[]{f, f1, BlockFace.UP}));
        blocks.addAll(getBlocks(b2, new BlockFace[]{f, f2, BlockFace.UP}));
        if(settings.butterfly)
        {
            blocks.add(getBlock(b1, f1));
            blocks.add(getBlock(b1, f2));
            blocks.add(getBlock(b2, f1));
            blocks.add(getBlock(b2, f2));
        }
        return blocks;
    }

    private Block getBlock(Block b, BlockFace face){
        Block block = b;
        for(int i = 0; i <( settings.layer + 1); i++) block = block.getRelative(face);
        return block;
    }

    private List<Block> getBlocks(Block b, BlockFace[] faces)
    {
        List<Block> blocks = new ArrayList<>();
        for(int i1 = 0; i1 <= settings.layer; i1++)
        {
            for (int i2 = 0; i2 <= settings.layer - i1; i2++)
            {
                Block block = b;
                int i3 = settings.layer - i1 - i2;
                for (int i = 0; i < i1; i++) block = block.getRelative(faces[0]);
                for (int i = 0; i < i2; i++) block = block.getRelative(faces[1]);
                for (int i = 0; i < i3; i++) block = block.getRelative(faces[2]);
                blocks.add(block);
            }
        }
        return blocks;
    }

    private BlockFace getNextFace(BlockFace face)
    {
        if(face == BlockFace.SOUTH || face == BlockFace.NORTH) return BlockFace.EAST;
        else return BlockFace.SOUTH;
    }

    @Override
    public int getSlot()
    {
        return 0;
    }

    @Override
    public ItemStack getItem()
    {
        return null;
    }
}
