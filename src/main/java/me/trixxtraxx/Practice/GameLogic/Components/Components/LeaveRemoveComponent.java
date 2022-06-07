package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveRemoveComponent extends GameComponent
{
    public LeaveRemoveComponent(GameLogic logic)
    {
        super(logic);
    }
    
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onLeave(PlayerQuitEvent event)
    {
        logic.removePlayer(event.getPlayer(), true);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onLeave(PlayerChangedWorldEvent event)
    {
        logic.removePlayer(event.getPlayer(), true);
    }
}
