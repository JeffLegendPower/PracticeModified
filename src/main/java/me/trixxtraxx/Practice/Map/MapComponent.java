package me.trixxtraxx.Practice.Map;

import me.trixxtraxx.Practice.Component;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.lang.reflect.Field;

public abstract class MapComponent extends Component
{
    protected Map map;

    public MapComponent(Map map)
    {
        this.map = map;
        map.addComponent(this);
    }
}
