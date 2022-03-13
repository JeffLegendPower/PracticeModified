package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Events;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

public class WinEvent extends GameEvent
{
    private Player p;
    public WinEvent(GameLogic logic, Player p)
    {
        super(logic);
        this.p = p;
    }

    public Player getPlayer(){return p;}
}
