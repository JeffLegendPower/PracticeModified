package me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Components;

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

import java.util.HashMap;
import java.util.Map;

public class PointComponentFFA extends GameComponent
{
    @Config
    private int maxPoints;
    
    HashMap<Player, Integer> pointMap = new HashMap<>();
    HashMap<Player, Player> lastHit = new HashMap<>();
    
    
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
                ffaLogic.win(killer);
            }
        }
    }
    
    @Override
    public String applyPlaceholder(Player p, String s)
    {
        String newS = s;
        for(Map.Entry<Player, Integer> entry : pointMap.entrySet())
        {
            newS = newS
                    .replace("{Points" + entry.getKey().getName() + "}", String.valueOf(entry.getValue()))
                    .replace("{Points" + entry.getKey().getName() + "Player}", entry.getKey().getName());
        }
        //replace the placeholders above with "" if they dont exist
        for(int i = pointMap.size(); i < logic.getPlayers().size(); i++)
        {
            newS = newS
                    .replace("{Points" + logic.getPlayers().get(i).getName() + "}", "")
                    .replace("{Points" + logic.getPlayers().get(i).getName() + "Player}", "");
        }
        return newS;
    }
}
