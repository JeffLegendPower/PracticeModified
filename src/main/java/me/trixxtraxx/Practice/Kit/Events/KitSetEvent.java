package me.trixxtraxx.Practice.Kit.Events;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.SQL.PracticePlayer;

public class KitSetEvent extends GameEvent
{
    //contains a PracticePlayer and the kit
    private Kit kit;
    private PracticePlayer player;
    
    public KitSetEvent(GameLogic logic, Kit kit, PracticePlayer player)
    {
        super(logic);
        this.kit = kit;
        this.player = player;
    }
    
    public Kit getKit()
    {
        return kit;
    }
    
    public PracticePlayer getPlayer()
    {
        return player;
    }
}
