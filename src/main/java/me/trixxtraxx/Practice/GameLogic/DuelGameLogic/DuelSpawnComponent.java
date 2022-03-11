package me.trixxtraxx.Practice.GameLogic.DuelGameLogic;

import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class DuelSpawnComponent implements ISpawnComponent
{
    private Location loc1;
    private Location loc2;

    private HashMap<Player, Location> spawns = new HashMap<>();

    public void init(Location loc1,Location loc2, List<Player> t1, List<Player> t2)
    {
        this.loc1 = loc1;
        this.loc2 = loc2;
        for (Player p:t1) spawns.put(p, this.loc1);
        for (Player p:t2) spawns.put(p, this.loc2);
    }

    @Override
    public Location getSpawn(GameLogic logic, Player p)
    {
        return spawns.get(p);
    }
}