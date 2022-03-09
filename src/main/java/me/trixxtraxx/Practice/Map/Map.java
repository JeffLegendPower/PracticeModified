package me.trixxtraxx.Practice.Map;

import me.trixxtraxx.Practice.Practice;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Map
{
    private static List<Map> maps;
    private String Name;
    private String LoadName;
    private ISpawnComponent spawn;
    private World world;
    protected List<MapComponent> components = new ArrayList<>();

    public Map(String name, String load, ISpawnComponent s)
    {
        Name = name;
        LoadName = load;
        spawn = s;
    }

    public World load()
    {
        world = Practice.worldLoader.loadWorld(LoadName);
        return world;
    }

    public void unload()
    {
        Bukkit.unloadWorld(world, false);
    }

    public String getName()
    {
        return Name;
    }
    public ISpawnComponent getSpawn(){return spawn;}

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
}
