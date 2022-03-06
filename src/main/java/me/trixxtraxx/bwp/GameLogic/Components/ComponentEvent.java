package me.trixxtraxx.bwp.GameLogic.Components;

import me.trixxtraxx.bwp.GameLogic.GameLogic;

public abstract class ComponentEvent
{
    private GameLogic logic;
    public ComponentEvent(GameLogic logic)
    {
        this.logic = logic;
    }

    public GameLogic getlogic(){return logic;}
}
