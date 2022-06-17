package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityResetComponent extends GameComponent
{
    public EntityResetComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onReset(ResetEvent event){
        //clear all entities that arent player
        for(Entity e : logic.getWorld().getEntities()){
            if(!(e instanceof Player)){
                e.remove();
            }
        }
    }
}
