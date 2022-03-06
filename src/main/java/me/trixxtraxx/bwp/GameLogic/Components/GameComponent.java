package me.trixxtraxx.bwp.GameLogic.Components;

import me.trixxtraxx.bwp.GameLogic.GameLogic;
import org.bukkit.event.*;

public abstract class GameComponent
{
    protected GameLogic logic;

    public GameComponent(GameLogic logic)
    {
        this.logic = logic;
    }

    public abstract void onEvent(GameEvent e);
    public abstract void onEvent(Event e);
}