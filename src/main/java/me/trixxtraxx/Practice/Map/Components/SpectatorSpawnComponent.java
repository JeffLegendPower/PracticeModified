package me.trixxtraxx.Practice.Map.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.ConfigLocation;

public class SpectatorSpawnComponent extends MapComponent
{
    @Config
    public ConfigLocation loc;
    
    public SpectatorSpawnComponent(Map m) {
        super(m);
    }
    
    public SpectatorSpawnComponent(Map m, ConfigLocation loc) {
        super(m);
        this.loc = loc;
    }
    
}
