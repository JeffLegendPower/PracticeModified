package me.trixxtraxx.Practice.GameEvents.AllModes;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

public class SpectatorRespawnEvent extends GameEvent
{
    private Player p;
    
    public SpectatorRespawnEvent(GameLogic logic, Player p)
    {
        super(logic);
    }
    
    public Player getPlayer()
    {
        return p;
    }
}
