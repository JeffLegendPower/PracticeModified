package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class StartInventoryComponent extends GameComponent
{
    public StartInventoryComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(StartEvent e)
    {
        Practice.log(4, "Setting Inventory on Start!");
        for (Player p:logic.getPlayers()) e.getlogic().getGame().getKit().setInventory(p);
    }
}
