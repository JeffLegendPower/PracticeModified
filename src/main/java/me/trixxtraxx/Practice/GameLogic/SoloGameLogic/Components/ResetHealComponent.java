package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
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
