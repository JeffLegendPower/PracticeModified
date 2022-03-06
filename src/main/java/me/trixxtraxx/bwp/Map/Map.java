package me.trixxtraxx.bwp.Map;

import com.google.gson.Gson;
import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.SQL.SQLUtil;
import me.trixxtraxx.bwp.Utils;
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

    public Map(String name, String load, ISpawnComponent s)
    {
        Name = name;
        LoadName = load;
        spawn = s;
    }

    public World load()
    {
        world = BWP.worldLoader.loadWorld(LoadName);
        return world;
    }

    public String getName()
    {
        return Name;
    }
    public ISpawnComponent getSpawn(){return spawn;}

    public static void init()
    {

    }
}
