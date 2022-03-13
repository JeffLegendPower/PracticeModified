package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NoDieComponent extends GameComponent
{
    public NoDieComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerDeathEvent) onQuit((PlayerDeathEvent) event);
    }

    public void onQuit(PlayerDeathEvent e)
    {
        e.getEntity().setHealth(20);
    }
}
