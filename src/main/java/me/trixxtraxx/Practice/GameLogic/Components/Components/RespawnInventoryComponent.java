package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;

public class RespawnInventoryComponent extends GameComponent
{
    public RespawnInventoryComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onRespawn(ToSpawnEvent event)
    {
        event.getPlayer().getInventory().clear();
        logic.getGame().getKit().setInventory(event.getPlayer());
    }
}
