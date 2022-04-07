package me.trixxtraxx.Practice.Map;

import me.trixxtraxx.Practice.ComponentClass;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Practice;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Map extends ComponentClass<MapComponent>
{
    private int id;
    private String Name;
    private String LoadName;
    private ISpawnComponent spawn;
    private World world;

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
}
