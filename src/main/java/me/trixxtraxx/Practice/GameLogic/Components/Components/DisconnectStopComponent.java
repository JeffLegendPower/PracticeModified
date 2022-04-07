package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectStopComponent extends GameComponent
{
    public DisconnectStopComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(PlayerQuitEvent e)
    {
        Practice.log(3, "Stoping Game due to a disconnect");
        logic.stop(true);
    }
}
