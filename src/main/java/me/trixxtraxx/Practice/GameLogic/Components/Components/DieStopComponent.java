package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DieStopComponent extends GameComponent
{
    public DieStopComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(PlayerDeathEvent e)
    {
        Practice.log(3, "Player Death: " + e.getEntity().getName());
        if (logic instanceof DuelGameLogic) ((DuelGameLogic) logic).remove(e.getEntity(), false);
        else logic.stop(false);
    }
}
