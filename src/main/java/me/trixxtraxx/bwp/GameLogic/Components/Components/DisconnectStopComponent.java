package me.trixxtraxx.bwp.GameLogic.Components.Components;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectStopComponent extends GameComponent
{
    public DisconnectStopComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerQuitEvent) onQuit((PlayerQuitEvent) event);
    }

    public void onQuit(PlayerQuitEvent e)
    {
        BWP.log(3, "Stoping Game due to a disconnect");
        logic.stop();
    }
}
