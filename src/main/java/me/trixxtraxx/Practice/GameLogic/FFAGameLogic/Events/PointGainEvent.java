package me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Events;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

public class PointGainEvent extends GameEvent
{
    private int newpoints;
    private Player player;
    
    public PointGainEvent(GameLogic logic, final Player player, final int newpoints) {
        super(logic);
        this.player = player;
        this.newpoints = newpoints;
    }
    
    public int getNewPoints() {
        return this.newpoints;
    }
    
    public Player getPlayer() {
        return this.player;
    }
}
