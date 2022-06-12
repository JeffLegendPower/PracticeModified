package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomKbComponent extends GameComponent
{
    @Config
    public static double global = 1.0;
    @Config
    public static double horizontal = 0.8;
    @Config
    public static double air = 1.1;
    @Config
    public static double vertical = 0.35;
    @Config
    public static double airVertical = 0.35;
    @Config
    public static double sprint = 0.6;
    @Config
    public static double walk = 0.8;
    
    public CustomKbComponent(GameLogic gameLogic, double global, double horizontal, double air, double vertical, double sprint, double walk)
    {
        super(gameLogic);
        this.global = global;
        this.horizontal = horizontal;
        this.air = air;
        this.vertical = vertical;
        this.sprint = sprint;
        this.walk = walk;
    }
    
    public CustomKbComponent(GameLogic logic)
    {
        super(logic);
    }
    
    private HashMap<Player, Player> lastDamager = new HashMap<>();
    private List<Player> sprintReset = new ArrayList<>();
    private HashMap<Player, List<Long>> hitStack = new HashMap<>();
    private List<Player> sprinting = new ArrayList<>();
    
    @TriggerEvent(state = TriggerEvent.CancelState.NONE)
    public void onPlayerVelocity(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            lastDamager.put((Player) e.getEntity(), (Player) e.getDamager());
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    lastDamager.remove(e.getEntity());
                }
            }.runTaskLater(Practice.Instance, 0);
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onPlayerMove(PlayerVelocityEvent e)
    {
        if(lastDamager.containsKey(e.getPlayer()))
        {
            Player damager = lastDamager.get(e.getPlayer());
            Location dmg = lastDamager.get(e.getPlayer()).getLocation();
            Player vik = e.getPlayer();
            Location pl = e.getPlayer().getLocation();
            
            //calculate the vector from dmg to pl
            Vector dir = pl.toVector().subtract(dmg.toVector());
            
            dir = dir.setY(0);
            //normalize
            dir = dir.normalize();
            //multiple by horizontal
            dir = dir.multiply(horizontal);
            //multiple by ground if on ground
            if(!vik.isOnGround())
            {
                dir = dir.multiply(air);
            }
            
            if(sprinting.contains(damager))
            {
                if(sprintReset.contains(damager))
                {
                    dir = dir.multiply(sprint);
                }
                else
                {
                    sprintReset.add(damager);
                }
            }
            else
            {
                dir = dir.multiply(walk);
            }
            
            
            //add vertical velocity
            if(vik.isOnGround()) dir = dir.setY(vertical);
            else dir = dir.setY(airVertical);
            
            //multiply by global
            dir = dir.multiply(global);
            
            //set the velocity of the player to the vector
            e.setVelocity(dir);
            
            lastDamager.remove(e.getPlayer());
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onSprintReset(PlayerToggleSprintEvent e)
    {
        if(!e.isSprinting())
        {
            sprintReset.remove(e.getPlayer());
            sprinting.remove(e.getPlayer());
        }
        else
        {
            sprinting.add(e.getPlayer());
        }
    }
}
