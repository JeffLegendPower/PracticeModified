package me.trixxtraxx.Practice.GameLogic.Components;

import me.trixxtraxx.Practice.Component;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

import java.lang.reflect.Field;

public abstract class GameComponent extends Component
{
    protected GameLogic logic;

    public GameComponent(GameLogic logic)
    {
        this.logic = logic;
        logic.addComponent(this);
    }
}