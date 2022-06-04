package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Bots;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotComponent extends GameComponent
{
    public double global = 0.5;
    public double horizontal = 0.8;
    public double air = 1.1;
    public double vertical = 0.35;
    public double sprint = 0.6;
    public double walk = 0.8;
    
    private List<Entity> sprintReset = new ArrayList<>();
    private List<Entity> sprinting = new ArrayList<>();
    
    
    public boolean cancel = false;
    boolean ret = false;
    public BotComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
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
    
    @TriggerEvent
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if(ret) return;
        if(logic instanceof BotGameLogic)
        {
            BotGameLogic botLogic = (BotGameLogic) logic;
            if(botLogic.getBot().bot.getEntity() == event.getEntity())
            {
                if(cancel)
                {
                    Practice.log(4, "Cancel hit due to noDamageTicks");
                    event.setCancelled(true);
                }
                else
                {
                    Practice.log(4, "Set no damage ticks for Bot " + event.getClass().getName());
                    cancel = true;
                    ret = true;
                    //delay by 5 ticks async with the bukkit scheduler cuz spigot sucks
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Practice.Instance, () -> cancel = false, 10);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Practice.Instance, () -> ret = false, 0);
                    Entity bot = botLogic.bot.bot.getEntity();
                    bot.setVelocity(getKb(event.getDamager().getLocation(), bot.getLocation(), event.getDamager(), bot));
                }
            }
        }
    }
    
    public Vector getKb(Location dmg, Location pl, Entity damager, Entity vik)
    {
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
        dir = dir.setY(vertical);
        //multiply by global
        dir = dir.multiply(global);
    
        //set the velocity of the player to the vector
        return dir;
    }
}
