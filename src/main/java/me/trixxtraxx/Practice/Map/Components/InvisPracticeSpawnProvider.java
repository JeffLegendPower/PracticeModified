package me.trixxtraxx.Practice.Map.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class InvisPracticeSpawnProvider extends MapComponent
{
    @Config
    private ConfigLocation loc;
    
    public InvisPracticeSpawnProvider(Map map, ConfigLocation loc)
    {
        super(map);
        this.loc = loc;
    }
    
    public InvisPracticeSpawnProvider(Map map)
    {
        super(map);
    }
    
    public Location getLocation()
    {
        return loc.getLocation(map.getWorld());
    }
}
