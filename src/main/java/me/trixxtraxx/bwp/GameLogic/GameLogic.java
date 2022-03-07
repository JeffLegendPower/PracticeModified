package me.trixxtraxx.bwp.GameLogic;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.GameEvents.GameEvent;

import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.Gamemode.Game;
import me.trixxtraxx.bwp.Map.Map;
import me.trixxtraxx.bwp.Map.MapComponent;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class GameLogic
{
    protected List<GameComponent> components = new ArrayList<>();

    public GameLogic() {}

    public List<GameComponent> getComponents()
    {
        return components;
    }

    public void addComponent(GameComponent comp)
    {
        components.add(comp);
    }

    public void removeComponent(GameComponent comp)
    {
        components.remove(comp);
    }

    public Event triggerEvent(Event e)
    {
        BWP.log(5, "Event triggered: " + e.getEventName());
        for (MapComponent comp : getMap().getComponents()) comp.onEvent(e);
        for (GameComponent comp : getComponents()) comp.onEvent(e);
        for (MapComponent comp : getMap().getComponents()) comp.onEventAfter(e);
        for (GameComponent comp : getComponents()) comp.onEventAfter(e);
        return e;
    }

    public GameEvent triggerEvent(GameEvent e)
    {
        BWP.log(5, "Event triggered: " + e.getClass().getName());
        for (MapComponent comp : getMap().getComponents()) comp.onEvent(e);
        for (GameComponent comp : getComponents()) comp.onEvent(e);
        for (MapComponent comp : getMap().getComponents()) comp.onEventAfter(e);
        for (GameComponent comp : getComponents()) comp.onEventAfter(e);
        if(e.isCanceled()) BWP.log(5, "Canceled");
        return e;
    }


    public abstract void start(Game gm, List<Player> players);
    public abstract void stop();
    public abstract World getWorld();
    public abstract List<Player> getPlayers();
    public abstract Game getGame();
    public abstract Map getMap();
}
