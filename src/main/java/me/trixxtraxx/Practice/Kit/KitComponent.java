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

    public void onEvent(GameEvent e){}
    public void onEvent(Event e){}

    public void onEventAfter(GameEvent e){}
    public void onEventAfter(Event e){}

    public String applyPlaceholder(Player p, String s){return s;}
}