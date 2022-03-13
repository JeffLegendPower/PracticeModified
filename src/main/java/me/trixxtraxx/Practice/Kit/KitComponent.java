package me.trixxtraxx.Practice.Kit;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class KitComponent
{
    protected Kit kit;

    public KitComponent(Kit kit)
    {
        this.kit = kit;
        kit.addComponent(this);
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