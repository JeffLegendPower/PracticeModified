package me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Events.PointGainEvent;
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.FFALogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PointComponentFFA extends GameComponent implements IStatComponent
{
    @Config
    private int maxPoints;
    
    protected HashMap<Player, Integer> pointMap = new HashMap<>();
    protected HashMap<Player, Player> lastHit = new HashMap<>();
    protected int initialPlayers = 0;
    
    public PointComponentFFA(GameLogic logic, int maxPoints){
        super(logic);
        this.maxPoints = maxPoints;
    }
    public PointComponentFFA(GameLogic gameLogic)
    {
        super(gameLogic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onPlayerDeath(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Projectile)) return;
        Player player = (Player) event.getEntity();
        Player killer = null;
        if(event.getDamager() instanceof Player) killer = (Player) event.getDamager();
        else if(event.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) event.getDamager();
            if(projectile.getShooter() instanceof Player) killer = (Player) projectile.getShooter();
            else return;
        }
        
        lastHit.put(player, killer);
        
        //remove after 2s
        Practice.Instance.getServer().getScheduler().runTaskLater(Practice.Instance, () -> lastHit.remove(player), 20 * 2);
    }
    
    @TriggerEvent
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = (Player) event.getEntity();
        Player killer = lastHit.get(player);
        if(killer == null) return;
        
        Object p = pointMap.get(killer);
        int points = 0;
        if(p != null) {
            points = (int) p;
        }
        points++;
        if(logic.triggerEvent(new PointGainEvent(logic, killer, points)).isCanceled()) return;
        pointMap.put(killer, points);
        
        if(points >= maxPoints)
        {
            if(logic instanceof FFALogic)
            {
                FFALogic ffaLogic = (FFALogic) logic;
                ffaLogic.win(killer, false);
            }
        }
    }
    
    @Override
    public String applyPlaceholder(Player p, String s)
    {
        if(logic.getPlayers().size() > initialPlayers) initialPlayers = logic.getPlayers().size();
        String newS = s;
        int index = 1;
        for(Map.Entry<Player, Integer> entry : pointMap.entrySet())
        {
            
            newS = newS
                    .replace("{Points" + index + "}", String.valueOf(entry.getValue()))
                    .replace("{Points" + index + "Player}", entry.getKey().getName());
            index++;
        }
        //replace the placeholders above with "" if they dont exist
        for(int i = pointMap.size() + 1; i < initialPlayers + 1; i++)
        {
            newS = newS
                    .replace("{Points" + i + "}", "0")
                    .replace("{Points" + i + "Player}", "");
        }
        //add {Place}, {Name}, {Points}
        //sort pointmap by points
        List<Map.Entry<Player, Integer>> list = new List();
        for(Map.Entry<Player, Integer> entry : pointMap.entrySet())
        {
            list.add(entry);
        }
        Collections.sort(list, (a, b) -> b.getValue().compareTo(a.getValue()));
        //init String place from the index of Player p in pointMap
        String place = "";
        //set place to x if the player is not in the list
        if(!pointMap.containsKey(p)) place = "x";
        else
        {
            index = 1;
            for(Map.Entry<Player, Integer> entry: list)
            {
                if(entry.getKey().equals(p))
                {
                    place = String.valueOf(index);
                    break;
                }
                index++;
            }
        }
        
        newS = newS
                .replace("{Place}", place)
                .replace("{Name}", p.getName())
                .replace("{Points}", String.valueOf(pointMap.get(p)));
        return newS;
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equalsIgnoreCase("Points")){
            return pointMap.get(p) == null ? "0" : String.valueOf(pointMap.get(p));
        }
        throw new IllegalArgumentException("Stat not found");
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        List<SQLProperty> list = new List();
        list.add(new SQLProperty("Points", "int(11)", "0", true));
        return list;
    }
}
