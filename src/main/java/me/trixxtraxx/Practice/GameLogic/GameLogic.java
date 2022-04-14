package me.trixxtraxx.Practice.GameLogic;

import me.trixxtraxx.Practice.ComponentClass;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Kit.KitComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;

import me.trixxtraxx.Practice.Gamemode.Game;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class GameLogic extends ComponentClass<GameComponent>
{
    protected int id = -1;
    protected String name;

    public GameLogic() {}

    public void setName(String s){name = s;}
    public void setId(int id){this.id = id;}

    public String getName(){return name;}
    public int getId(){return id;}
    
    @Override
    public <T> T triggerEvent(T e)
    {
        Practice.log(5, "Event: " + e.getClass().getSimpleName());
        getMap().triggerEvent(e);
        super.triggerEvent(e);
        getGame().getKit().triggerEvent(e);
        return e;
    }

    public abstract void start(Game gm, List<Player> players, Map m);
    public abstract void stop(boolean dc);
    public abstract void toSpawn(Player p);
    public abstract World getWorld();
    public abstract List<Player> getPlayers();
    public abstract Game getGame();
    public abstract Map getMap();
    public abstract void applyData(String s);
    public abstract String getData();
}
