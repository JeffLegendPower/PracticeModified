package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class StatCountingComponent extends GameComponent
{
    HashMap<Player, Object> stats = new HashMap<>();
    public StatCountingComponent(GameLogic logic) {super(logic);}

    public void setStat(Player p, Object o)
    {
        stats.put(p, o);
    }

    public Object getStats(Player p)
    {
        return stats.get(p);
    }

    public Object getStatsOrDefault(Player p, Object def)
    {
        Object s =  stats.get(p);
        if(s == null) return def;
        return s;
    }
}
