package me.trixxtraxx.Practice.Map;

import me.trixxtraxx.Practice.ComponentClass;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Practice;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;

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
        Practice.log(3, "Loading map " + Name);
        world = Practice.worldLoader.loadWorld(LoadName);
        Practice.log(3, "Loaded map " + Name + " new world " + world.getName());
        return world;
    }

    public void unload(boolean save)
    {
        Practice.log(3, "Unloading map " + Name);
        String worldName = world.getName();
        world = null;
        Practice.worldLoader.unloadWorld(Bukkit.getWorld(worldName));
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
