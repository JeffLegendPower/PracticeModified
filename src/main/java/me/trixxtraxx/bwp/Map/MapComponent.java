package me.trixxtraxx.bwp.Map;

import me.trixxtraxx.bwp.GameEvents.GameEvent;
import org.bukkit.event.Event;

public abstract class MapComponent
{
    protected Map map;

    public MapComponent(Map map)
    {
        this.map = map;
        map.addComponent(this);
    }

    public void onEvent(GameEvent e){}
    public void onEvent(Event e){}

    public void onEventAfter(GameEvent e){}
    public void onEventAfter(Event e){}
}
