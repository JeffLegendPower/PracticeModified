package me.trixxtraxx.bwp.Kit;

import me.trixxtraxx.bwp.GameEvents.GameEvent;
import me.trixxtraxx.bwp.Map.Map;
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
}
