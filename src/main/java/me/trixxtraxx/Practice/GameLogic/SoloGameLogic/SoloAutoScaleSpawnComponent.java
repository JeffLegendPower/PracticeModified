package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

import com.google.gson.Gson;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.Components.AutoScaleComponent;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SoloAutoScaleSpawnComponent implements ISpawnComponent
{
    private ConfigLocation loc;
    
    public SoloAutoScaleSpawnComponent(Location loc)
    {
        this.loc = new ConfigLocation(loc);
    }
    
    public SoloAutoScaleSpawnComponent() {}
    
    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        if(!(logic instanceof SoloAutoscaleLogic)) return null;
        SoloAutoscaleLogic soloLogic = (SoloAutoscaleLogic) logic;
        List<MapComponent> components = soloLogic.getMap().getComponents(AutoScaleComponent.class);
        if(components.size() == 0) return loc.getLocation(logic.getWorld());
        AutoScaleComponent comp = (AutoScaleComponent) components.get(0);
        return comp.convertLoc(loc, soloLogic.getScale()).getLocation(logic.getWorld());
    }
    
    @Override
    public String getData()
    {
        return new Gson().toJson(loc);
    }
    
    @Override
    public void applyData(String s)
    {
        loc = new Gson().fromJson(s, ConfigLocation.class);
    }
}
