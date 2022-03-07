package me.trixxtraxx.bwp.GameLogic.Components;

import me.trixxtraxx.bwp.GameLogic.GameLogic;
import org.bukkit.event.*;

public abstract class GameComponent
{
    protected GameLogic logic;

    public GameComponent(GameLogic logic)
    {
        this.logic = logic;
        logic.addComponent(this);
    }

    public void onEvent(GameEvent e){}
    public void onEvent(Event e){}

    public void onEventAfter(GameEvent e){}
    public void onEventAfter(Event e){}
}