package me.trixxtraxx.bwp.GameLogic.Components.Components;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.bwp.GameEvents.GameEvent;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import org.bukkit.entity.Player;

public class StartInventoryComponent extends GameComponent
{
    public StartInventoryComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(GameEvent event){
        if(event instanceof StartEvent) onStart((StartEvent) event);
    }

    public void onStart(StartEvent e)
    {
        BWP.log(4, "Setting Inventory on Start!");
        for (Player p:logic.getPlayers()) e.getlogic().getGame().getKit().setInventory(p);
    }
}
