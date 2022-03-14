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
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class GameLogic
{
    protected List<GameComponent> components = new ArrayList<>();
    protected int id;
    protected String name;

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

    public List<GameComponent> getComponents(Class<?> c)
    {
        List<GameComponent> comps = new ArrayList<>();
        for (GameComponent comp:components)
        {
            if(c.isInstance(comp)) comps.add(comp);
        }
        return comps;
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
        if(e instanceof Cancellable){
            Cancellable c = (Cancellable) e;
            if(c.isCancelled()){
                for (MapComponent comp : getMap().getComponents()) comp.onEventCancel(e);
                for (GameComponent comp : getComponents()) comp.onEventCancel(e);
                for (KitComponent comp : getGame().getKit().getComponents()) comp.onEventCancel(e);
            }
        }
        return e;
    }

    public GameEvent triggerEvent(GameEvent e)
    {
        Practice.log(5, "Event triggered: " + e.getClass().getName());
        for (MapComponent comp : getMap().getComponents()) comp.onEvent(e);
        for (GameComponent comp : getComponents()) comp.onEvent(e);
        for (KitComponent comp : getGame().getKit().getComponents()) comp.onEvent(e);
        if (e.isCanceled())
        {
            for (MapComponent comp : getMap().getComponents()) comp.onEventCancel(e);
            for (GameComponent comp : getComponents()) comp.onEventCancel(e);
            for (KitComponent comp : getGame().getKit().getComponents()) comp.onEventCancel(e);
        } else
        {
            for (MapComponent comp : getMap().getComponents()) comp.onEventAfter(e);
            for (GameComponent comp : getComponents()) comp.onEventAfter(e);
            for (KitComponent comp : getGame().getKit().getComponents()) comp.onEventAfter(e);
        }
        if (e.isCanceled()) Practice.log(5, "Canceled");
        return e;
    }

    public String applyPlaceholders(Player p, String s)
    {
        for (MapComponent comp : getMap().getComponents()) s = comp.applyPlaceholder(p, s);
        for (GameComponent comp : getComponents()) s = comp.applyPlaceholder(p, s);
        for (KitComponent comp : getGame().getKit().getComponents()) s = comp.applyPlaceholder(p, s);
        return s;
    }

    public List<String> applyPlaceholders(Player p, List<String> list)
    {
        List<String> newStrings = new ArrayList<>();
        for (String string:list)
        {
            String s = string;
            for (MapComponent comp : getMap().getComponents()) s = comp.applyPlaceholder(p, s);
            for (GameComponent comp : getComponents()) s = comp.applyPlaceholder(p, s);
            for (KitComponent comp : getGame().getKit().getComponents()) s = comp.applyPlaceholder(p, s);
            newStrings.add(s);
        }
        return newStrings;
    }

    public void setName(String s){name = s;}
    public void setId(int id){this.id = id;}

    public String getName(){return name;}
    public int getId(){return id;}


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
