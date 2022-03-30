package me.trixxtraxx.Practice.Map;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Practice;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Map
{
    private int id;
    private String Name;
    private String LoadName;
    private ISpawnComponent spawn;
    private World world;
    protected List<MapComponent> components = new ArrayList<>();

    public Map(int id, String name, String load, ISpawnComponent s)
    {
        Name = name;
        LoadName = load;
        spawn = s;
        this.id = id;
    }

    public World load()
    {
        world = Practice.worldLoader.loadWorld(LoadName);
        return world;
    }

    public void unload(boolean save)
    {
        Bukkit.unloadWorld(world, save);
    }

    public String getName()
    {
        return Name;
    }

    public String getLoad()
    {
        return LoadName;
    }

    public int getSqlIndex()
    {
        return id;
    }

    public ISpawnComponent getSpawn() {return spawn;}

    public World getWorld() {return world;}

    public static void init()
    {

    }

    public List<MapComponent> getComponents()
    {
        return components;
    }

    public void addComponent(MapComponent comp)
    {
        components.add(comp);
    }

    public void removeComponent(MapComponent comp)
    {
        components.remove(comp);
    }

    public List<MapComponent> getComponents(Class<?> c)
    {
        List<MapComponent> comps = new ArrayList<>();
        for (MapComponent comp : components)
        {
            if (c.isInstance(comp) || c.isAssignableFrom(comp.getClass())) comps.add(comp);
        }
        return comps;
    }
}
