package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class NoArrowPickupComponent extends GameComponent
{
    
    public NoArrowPickupComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onArrowHit(ProjectileHitEvent event)
    {
        Practice.log(4, "Arrow hit");
        event.getEntity().remove();
    }
}
