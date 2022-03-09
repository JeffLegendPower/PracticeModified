package me.trixxtraxx.Practice.GameLogic;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Kit.KitComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;

import me.trixxtraxx.Practice.Gamemode.Game;
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
        Practice.log(5, "Event triggered: " + e.getEventName());
        for (MapComponent comp : getMap().getComponents()) comp.onEvent(e);
        for (GameComponent comp : getComponents()) comp.onEvent(e);
        for (KitComponent comp : getGame().getKit().getComponents()) comp.onEvent(e);
        for (MapComponent comp : getMap().getComponents()) comp.onEventAfter(e);
        for (GameComponent comp : getComponents()) comp.onEventAfter(e);
        for (KitComponent comp : getGame().getKit().getComponents()) comp.onEventAfter(e);
        return e;
    }

    public GameEvent triggerEvent(GameEvent e)
    {
        Practice.log(5, "Event triggered: " + e.getClass().getName());
        for (MapComponent comp : getMap().getComponents()) comp.onEvent(e);
        for (GameComponent comp : getComponents()) comp.onEvent(e);
        for (KitComponent comp : getGame().getKit().getComponents()) comp.onEvent(e);
        for (MapComponent comp : getMap().getComponents()) comp.onEventAfter(e);
        for (GameComponent comp : getComponents()) comp.onEventAfter(e);
        for (KitComponent comp : getGame().getKit().getComponents()) comp.onEventAfter(e);
        if(e.isCanceled()) Practice.log(5, "Canceled");
        return e;
    }


    public abstract void start(Game gm, List<Player> players);
    public abstract void stop();
    public abstract World getWorld();
    public abstract List<Player> getPlayers();
    public abstract Game getGame();
    public abstract Map getMap();
}
