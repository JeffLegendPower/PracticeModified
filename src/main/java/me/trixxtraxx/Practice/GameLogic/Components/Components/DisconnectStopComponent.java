package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectStopComponent extends GameComponent
{
    public DisconnectStopComponent(GameLogic logic)
    {
        super(logic);
    }

    public DisconnectStopComponent(GameLogic logic, String s) {super(logic);}

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerQuitEvent) onQuit((PlayerQuitEvent) event);
    }

    @Override
    public String getData() {return "{}";}

    public void onQuit(PlayerQuitEvent e)
    {
        Practice.log(3, "Stoping Game due to a disconnect");
        logic.stop(true);
    }
}
