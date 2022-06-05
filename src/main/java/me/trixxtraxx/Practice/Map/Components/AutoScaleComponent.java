package me.trixxtraxx.Practice.Map.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;

public class AutoScaleComponent extends MapComponent
{
    @Config
    private double xOffset;
    @Config
    private double yOffset;
    @Config
    private double zOffset;
    @Config
    private List<Region> regions;
    private HashMap<Region, List<BlockStorage>> blocks;
    
    public class BlockStorage{
        private Material mat;
        private byte data;
        
        public BlockStorage(Material mat, byte data){
            this.mat = mat;
            this.data = data;
        }
        
        public Material getMaterial(){
            return mat;
        }
        
        public byte getData(){
            return data;
        }
    }
    
    public AutoScaleComponent(Map map, double xOffset, double yOffset, double zOffset, List<Region> regions)
    {
        super(map);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.regions = regions;
    }
    
    public AutoScaleComponent(Map map)
    {
        super(map);
    }
    
    public ConfigLocation convertLoc(ConfigLocation loc, int scale)
    {
        Practice.log(4, "Converting location: " + loc.toString());
        return new ConfigLocation(loc.getX() + (scale * xOffset), loc.getY() + (scale * yOffset), loc.getZ() + (scale * zOffset), loc.getYaw(), loc.getPitch());
    }
    
    public Region convertLoc(Region reg, int scale)
    {
        return reg.shift((int) (scale * xOffset), (int) (scale * yOffset), (int) (scale * zOffset));
    }
    
    public void cloneWorld(int scale, World w)
    {
        Practice.log(4, "Cloning world: " + map.getName() + " with scale: " + scale);
        for(Region r : regions)
        {
            ConfigLocation newloc1 = new ConfigLocation(r.getLoc1().getX() + (xOffset * scale), r.getLoc1().getY() + (yOffset * scale), r.getLoc1().getZ() + (zOffset * scale), r.getLoc1().getYaw(), r.getLoc1().getPitch());
            ConfigLocation newloc2 = new ConfigLocation(r.getLoc2().getX() + (xOffset * scale), r.getLoc2().getY() + (yOffset * scale), r.getLoc2().getZ() + (zOffset * scale), r.getLoc2().getYaw(), r.getLoc2().getPitch());
    
            Region newRegion = new Region(newloc1, newloc2);
    
            List<BlockStorage> storage = blocks.get(r);
            List<Location> newLocs = newRegion.getLocations(w);
            //Practice.log(4, "Cloning " + storage + " blocks to " + newLocs + " locations");
    
            for(int i = 0; i < storage.size(); i++)
            {
                newLocs.get(i).getBlock().setType(storage.get(i).getMaterial());
                newLocs.get(i).getBlock().setData(storage.get(i).getData());
            }
        }
    }
    
    public void removeScale(int scale)
    {
        for(Region r : regions)
        {
            ConfigLocation newloc1 = new ConfigLocation(r.getLoc1().getX() + (xOffset * scale), r.getLoc1().getY() + (yOffset * scale), r.getLoc1().getZ() + (zOffset * scale), r.getLoc1().getYaw(), r.getLoc1().getPitch());
            ConfigLocation newloc2 = new ConfigLocation(r.getLoc2().getX() + (xOffset * scale), r.getLoc2().getY() + (yOffset * scale), r.getLoc2().getZ() + (zOffset * scale), r.getLoc2().getYaw(), r.getLoc2().getPitch());
    
            Region newRegion = new Region(newloc1, newloc2);
    
            List<Location> newLocs = newRegion.getLocations(map.getWorld());
    
            for(Location loc : newLocs)
            {
                loc.getBlock().setType(Material.AIR);
            }
        }
    }
    
    public void setStorage(World w){
        blocks = new HashMap<>();
        for(Region r : regions)
        {
            List<BlockStorage> storage = new List<BlockStorage>();
            for(Location l : r.getLocations(w))
            {
                storage.add(new BlockStorage(l.getBlock().getType(), l.getBlock().getData()));
            }
            blocks.put(r, storage);
        }
    }
    
    public HashMap<Region, List<BlockStorage>> getStorage(){
        return blocks;
    }
    
    public void setStorage(AutoScaleComponent scale)
    {
        blocks = scale.getStorage();
        regions = scale.regions;
    }
    
    @Override
    public String getData()
    {
        //serialize everything with @Config annotation and serialize each region seperately
        StringBuilder sb = new StringBuilder();
        sb.append(xOffset + "|").append(yOffset + "|").append(zOffset + "|");
        for(Region r : regions)
        {
            sb.append(r.serialize() + "|");
        }
        return sb.toString();
    }
    
    @Override
    public void applyData(String value)
    {
        String[] split = value.split("\\|");
        xOffset = Double.parseDouble(split[0]);
        yOffset = Double.parseDouble(split[1]);
        zOffset = Double.parseDouble(split[2]);
        regions = new List<>();
        for(int i = 3; i < split.length; i++)
        {
            String reg = split[i];
            if(reg.isEmpty()) continue;
            Practice.log(4, "Adding region: " + split[i]);
            regions.add(Region.deserialize(reg));
        }
    }
}
