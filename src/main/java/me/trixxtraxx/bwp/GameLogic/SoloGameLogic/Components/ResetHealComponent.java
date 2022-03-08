package me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.bwp.GameEvents.GameEvent;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.entity.Player;

public class ResetHealComponent extends GameComponent
{
    public ResetHealComponent(GameLogic logic)
    {
        super(logic);
    }

    public void onEvent(GameEvent event)
    {
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
    }

    public void onReset(ResetEvent e)
    {
        for (Player p:logic.getPlayers())
        {
            p.setHealth(p.getMaxHealth());
        }
    }
}
