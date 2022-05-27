package me.trixxtraxx.Practice.GameLogic.FFAGameLogic;

import com.google.gson.Gson;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.Practice;
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
        Location loc = spawns.get(currentIndex++).getLocation(logic.getWorld());
        Location finalLoc2 = loc;
        List<Player> players = logic.getPlayers().findAll(x -> x.getWorld() == finalLoc2.getWorld());
        int iteraction = 0;
        while(true)
        {
            iteraction++;
            if(iteraction > 100) break;
            Location finalLoc = loc;
            //increase by 1 if a player is within 5 blocks
            if(players.any(player -> player.getLocation().distance(finalLoc) < 5))
            {
                currentIndex++;
                if(currentIndex >= spawns.size())
                {
                    currentIndex = 0;
                }
                loc = spawns.get(currentIndex++).getLocation(logic.getWorld());
            }
            else
            {
                break;
            }
        }
        return loc;
    }
    
    @Override
    public String getData()
    {
        //serialize spawns
        String s = "";
        for(ConfigLocation loc : spawns)
        {
            s += loc.serialize() + "|";
        }
        Practice.log(4, "FFA Spawn Serialized: " + s);
        return s;
    }
    
    @Override
    public void applyData(String s)
    {
        //deserialize spawns
        String[] split = s.split("\\|");
        for(String loc : split)
        {
            if(loc == null || loc.isEmpty()) continue;
            spawns.add(ConfigLocation.deserialize(loc));
        }
    }
}
