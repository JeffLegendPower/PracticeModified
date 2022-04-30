package me.trixxtraxx.Practice.GameLogic.FFAGameLogic;

import com.google.gson.Gson;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FFASpawnComponent implements ISpawnComponent
{
    public List<ConfigLocation> spawns;
    int currentIndex = 0;
    
    public FFASpawnComponent(final List<ConfigLocation> spawns) {
        this.spawns = spawns;
    }
    
    public FFASpawnComponent()
    {
        this.spawns = new List<ConfigLocation>();
    }
    
    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        //return spawns from currentIndex and increase index by 1, if index is bigger than size of list, set index to 0
        if(currentIndex >= spawns.size())
        {
            currentIndex = 0;
        }
        return spawns.get(currentIndex++).getLocation(logic.getWorld());
    }
    
    @Override
    public String getData()
    {
        return new Gson().toJson(spawns);
    }
    
    @Override
    public void applyData(String s)
    {
        List<ConfigLocation> c = new List<>();
        spawns = new Gson().fromJson(s, c.getClass());
    }
}
