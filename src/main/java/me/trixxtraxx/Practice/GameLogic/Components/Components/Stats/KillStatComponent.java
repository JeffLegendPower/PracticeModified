package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class KillStatComponent extends GameComponent implements IStatComponent
{
    HashMap<Player, Integer> killMap = new HashMap<>();
    HashMap<Player, Integer> deathMap = new HashMap<>();
    
    public KillStatComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onKill(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if(killer != null) killMap.put(killer, killMap.getOrDefault(killer, 0) + 1);
        deathMap.put(player, deathMap.getOrDefault(player, 0) + 1);
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        List<SQLProperty> properties = new List<>();
        properties.add(new SQLProperty("kills", "int(11)", "0", false));
        properties.add(new SQLProperty("deaths", "int(11)", "0", false));
        properties.add(new SQLProperty("GameKills", "int(11)", "0", true));
        properties.add(new SQLProperty("GameDeaths", "int(11)", "0", true));
        return properties;
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equals("kills"))
        {
            return getBestAndAdd(p, logic.getName(), "kills", killMap.getOrDefault(p, 0));
        }
        if(stat.equals("deaths"))
        {
            return getBestAndAdd(p, logic.getName(), "deaths", deathMap.getOrDefault(p, 0));
        }
        if(stat.equals("GameKills"))
        {
            return killMap.getOrDefault(p, 0) + "";
        }
        if(stat.equals("GameDeaths"))
        {
            return deathMap.getOrDefault(p, 0) + "";
        }
        throw new IllegalArgumentException("Stat " + stat + " not found");
    }
}
