package me.trixxtraxx.Practice.GameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;
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

    public String applyPlaceholder(Player p, String s){return s;}
}