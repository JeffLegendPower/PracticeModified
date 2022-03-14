package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DieStopComponent extends GameComponent
{
    public DieStopComponent(GameLogic logic)
    {
        super(logic);
    }
    public DieStopComponent(GameLogic logic, String s) {super(logic);}

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerDeathEvent) onQuit((PlayerDeathEvent) event);
    }

    @Override
    public String getData()
    {
        return "";
    }

    public void onQuit(PlayerDeathEvent e)
    {
        if (logic instanceof DuelGameLogic) ((DuelGameLogic) logic).remove(e.getEntity());
        else logic.stop(false);
    }
}
