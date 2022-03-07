package me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.bwp.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.bwp.GameEvents.GameEvent;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.entity.Player;

public class InventoryOnResetComponent extends GameComponent
{
    public InventoryOnResetComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(GameEvent event){
        if(event instanceof ResetEvent) onStart((ResetEvent) event);
    }

    public void onStart(ResetEvent e)
    {
        for (Player p:logic.getPlayers()) e.getlogic().getGame().getKit().setInventory(p);
    }
}
