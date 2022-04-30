package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFallDamageComponent extends GameComponent
{
    
    public NoFallDamageComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity() instanceof Player)
        {
            event.setCancelled(true);
        }
    }
}
