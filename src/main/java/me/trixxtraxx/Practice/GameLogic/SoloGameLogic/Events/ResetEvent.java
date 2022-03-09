package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events;

import me.trixxtraxx.Practice.GameEvents.GameEvent;

import me.trixxtraxx.Practice.GameLogic.GameLogic;

public class ResetEvent extends GameEvent
{
    private boolean sucess;

    public ResetEvent(GameLogic logic, boolean sucess)
    {
        super(logic);
        this.sucess = sucess;
    }
}
