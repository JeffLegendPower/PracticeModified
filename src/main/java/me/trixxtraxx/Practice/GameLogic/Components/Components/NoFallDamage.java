package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFallDamage extends GameComponent
{
    public NoFallDamage(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onEvent(EntityDamageEvent e)
    {
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);
    }
}