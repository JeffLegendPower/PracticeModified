package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.FFALogic;
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
    public void onEvent(PlayerQuitEvent e) {
        Practice.log(3, "Stopping Game due to a disconnect");
        if (logic instanceof DuelGameLogic) {
            DuelGameLogic duelGameLogic = (DuelGameLogic) logic;
            if (duelGameLogic.getP1().getUniqueId() == e.getPlayer().getUniqueId()) {
                duelGameLogic.win(duelGameLogic.getP2(), true);
            } else {
                duelGameLogic.win(duelGameLogic.getP1(), true);
            }
        } else if (logic instanceof FFALogic) {
            FFALogic ffaLogic = (FFALogic) logic;
            ffaLogic.removePlayer(e.getPlayer(), true);
        } else {
            logic.stop(true);
        }
    }
}
