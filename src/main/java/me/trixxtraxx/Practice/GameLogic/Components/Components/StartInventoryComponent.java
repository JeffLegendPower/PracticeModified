package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

public class StartInventoryComponent extends GameComponent
{
    public StartInventoryComponent(GameLogic logic)
    {
        super(logic);
    }
    public StartInventoryComponent(GameLogic logic, String s)
    {
        super(logic);
    }
    @Override
    public String getData() {return "{}";}

    @Override
    public void onEvent(GameEvent event){
        if(event instanceof StartEvent) onStart((StartEvent) event);
    }

    public void onStart(StartEvent e)
    {
        Practice.log(4, "Setting Inventory on Start!");
        for (Player p:logic.getPlayers()) e.getlogic().getGame().getKit().setInventory(p);
    }
}
