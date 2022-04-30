package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoPlayerLootComponent extends GameComponent
{
    
    public NoPlayerLootComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        e.getDrops().clear();
    }
}
