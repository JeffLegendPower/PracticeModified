package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class NoKillitemDrop extends GameComponent
{
    
    public NoKillitemDrop(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onDeath(EntityDeathEvent event){
        event.getDrops().clear();
    }
}
