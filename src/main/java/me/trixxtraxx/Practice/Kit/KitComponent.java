package me.trixxtraxx.Practice.Kit;

import me.trixxtraxx.Practice.Component;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.lang.reflect.Field;

public abstract class KitComponent extends Component
{
    protected Kit kit;

    public KitComponent(Kit kit)
    {
        this.kit = kit;
        kit.addComponent(this);
    }
}