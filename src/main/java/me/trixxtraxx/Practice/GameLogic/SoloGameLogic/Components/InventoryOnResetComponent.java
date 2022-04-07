package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.entity.Player;

public class InventoryOnResetComponent extends GameComponent
{
    public InventoryOnResetComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(GameEvent event){
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
    }

    public void onReset(ResetEvent e)
    {
        for (Player p:logic.getPlayers()) e.getlogic().getGame().getKit().setInventory(p);
    }
}
