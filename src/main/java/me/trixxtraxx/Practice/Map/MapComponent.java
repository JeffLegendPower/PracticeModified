package me.trixxtraxx.Practice.Map;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class MapComponent
{
    protected Map map;

    public MapComponent(Map map)
    {
        this.map = map;
        map.addComponent(this);
    }

    public abstract String getData();

    public void onEvent(GameEvent e){}
    public void onEvent(Event e){}

    public void onEventAfter(GameEvent e){}
    public void onEventAfter(Event e){}

    public void onEventCancel(GameEvent e){}
    public void onEventCancel(Event e){}

    public String applyPlaceholder(Player p, String s){return s;}
}
