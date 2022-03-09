package me.trixxtraxx.Practice.GameEvents;

import me.trixxtraxx.Practice.GameLogic.GameLogic;

public abstract class GameEvent
{
    private GameLogic logic;
    private boolean canceled = false;

    public GameEvent(GameLogic logic)
    {
        this.logic = logic;
    }

    public GameLogic getlogic(){return logic;}

    public void setCanceled(boolean b) {canceled = b;}
    public boolean isCanceled() {return canceled;}
}
