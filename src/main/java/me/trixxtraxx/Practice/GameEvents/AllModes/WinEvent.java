package me.trixxtraxx.Practice.GameEvents.AllModes;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

public class WinEvent extends GameEvent
{
    private Player p;
    private boolean force;
    
    public WinEvent(GameLogic logic, Player p, boolean force)
    {
        super(logic);
        this.p = p;
        this.force = force;
    }

    public Player getPlayer(){return p;}
    
    public boolean isForced(){return force;}
}
