package me.trixxtraxx.Practice.GameEvents.AllModes;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;

public class StopEvent extends GameEvent
{
    private boolean isDisconnect;
    public StopEvent(GameLogic logic, boolean isDisconnect)
    {
        super(logic);
        this.isDisconnect = isDisconnect;
    }

    public boolean isDisconnect(){return isDisconnect;}
}