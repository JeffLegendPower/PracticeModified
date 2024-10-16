package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoDamageComponent extends GameComponent
{

    public NoDamageComponent(GameLogic logic) {
        super(logic);
    }

    @TriggerEvent
    public void onDamage(EntityDamageEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            event.setDamage(0);
        }
    }
}