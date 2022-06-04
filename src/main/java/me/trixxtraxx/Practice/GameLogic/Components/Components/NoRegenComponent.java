package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class NoRegenComponent extends GameComponent
{
    
    public NoRegenComponent(GameLogic logic)
    {
        super(logic);
    }
    
    
    @TriggerEvent
    public void onRegen(EntityRegainHealthEvent event)
    {
        if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED && event.getEntity() instanceof Player)
        {
            event.setCancelled(true);
        }
    }
}
