package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnInventoryComponent extends GameComponent
{
    public RespawnInventoryComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onRespawn(ToSpawnEvent event)
    {
        Practice.log(4, "Setting inventory for " + event.getPlayer().getName());
        logic.getGame().getKit().setInventory(event.getPlayer());
    }
}
