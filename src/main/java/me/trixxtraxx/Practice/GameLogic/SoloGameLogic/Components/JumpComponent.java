package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PlayerStats;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.TriggerEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class JumpComponent extends GameComponent implements IStatComponent
{
    @Config
    private Material material;
    private double lastDistance;
    
    public JumpComponent(GameLogic logic, Material goal)
    {
        super(logic);
        this.material = goal;
    }
    
    public JumpComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onGoalHit(PlayerMoveEvent event)
    {
        if(event.getTo().getY() % 1 != 0) return;
        if(event.getTo().clone().add(0, -0.01, 0).getBlock().getType().equals(material) ||
                event.getTo().clone().add(0.3, -0.01, 0.3).getBlock().getType().equals(material) ||
                event.getTo().clone().add(-0.3, -0.01, -0.3).getBlock().getType().equals(material) ||
                event.getTo().clone().add(0.3, -0.01, -0.3).getBlock().getType().equals(material) ||
                event.getTo().clone().add(-0.3, -0.01, 0.3).getBlock().getType().equals(material))
        {
            lastDistance = event.getTo().getX();
            //round to nearest .01
            lastDistance = Math.round(lastDistance * 100.0) / 100.0;
            if(logic instanceof SoloGameLogic)
            {
                SoloGameLogic solo = (SoloGameLogic) logic;
                solo.reset(true);
                solo.getPlayer().sendMessage(ChatColor.BLUE + "Your Jump was " + ChatColor.AQUA + lastDistance + ChatColor.BLUE + " blocks far!");
            }
        }
    }
    
    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{Distance}", String.valueOf(lastDistance));
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onTNT(BlockPlaceEvent event)
    {
        if(event.getBlockPlaced().getType().equals(Material.TNT))
        {
            event.getBlockPlaced().setType(Material.AIR);
            TNTPrimed tnt = event.getBlockPlaced().getWorld().spawn(event.getBlockPlaced().getLocation().add(.5,.5,.5), TNTPrimed.class);
            tnt.setFuseTicks(40);
        }
    }
    
    @TriggerEvent
    public void onTNTDMG(EntityDamageEvent event)
    {
        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION){
            event.setDamage(4);
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onTNT(EntityExplodeEvent event)
    {
        //TODO push player away from TNT
    }
    
    private int ticks = 20;
    private List<Player> cooldown = new List<>();
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void OnPlayerInteract(PlayerInteractEvent e)
    {
        if (e.getItem() != null && e.getItem().getType() == Material.FIREBALL)
        {
            Player p = e.getPlayer();
            e.setCancelled(true);
            if(cooldown.contains(p))
            {
                p.sendMessage(ChatColor.RED + "You can't use that yet!");
                return;
            }
            cooldown.add(p);
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    cooldown.remove(p);
                }
            }.runTaskLater(Practice.Instance, ticks);
            
            Fireball fireball = p.launchProjectile(Fireball.class);
            
            new BukkitRunnable(){
                @Override
                public void run()
                {
                    fireball.setVelocity(fireball.getVelocity().multiply(2));
                }
            }.runTaskLater(Practice.Instance, 1);
            
            if(e.getItem().getAmount() == 1)
            {
                p.getInventory().remove(e.getItem());
            }
            else
            {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            }
        }
    }
    
    private boolean cancelblockexplode = false;
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void OnFbExplode(EntityExplodeEvent e)
    {
        if(!(e.getEntity() instanceof Fireball)) return;
        Location loc = e.getLocation();
        HashMap<Entity, Vector> velocities = new HashMap<>();
        for (Entity en:e.getLocation().getWorld().getNearbyEntities(e.getLocation(), 20d,20d,20d))
        {
            velocities.put(en, en.getVelocity());
        }
        
        cancelblockexplode = true;
        Practice.log(4, "Creating explosion for fireball");
        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0, false, true);
        cancelblockexplode = false;
        
        for (Map.Entry<Entity, Vector> el : velocities.entrySet())
        {
            push(loc, el.getKey(), el.getValue());
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void OnFbExplode(EntityDamageEvent e)
    {
        if(cancelblockexplode && e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
        {
            e.setDamage(e.getDamage() / 5);
        }
    }
    
    private static void push(Location loc, Entity en, Vector original)
    {
        if(!(en instanceof Player)) return;
        double dist = loc.distance(en.getLocation());
        if(dist < 1.5d) dist = 1.5d;
        if(dist > 4) return;
        double smt = Math.pow(0.6d, dist);
        Vector vel = new Vector(
                en.getLocation().getX() - loc.getX(),
                en.getLocation().getY() - loc.getY(),
                en.getLocation().getZ() - loc.getZ());
        vel.normalize();
        vel.multiply(smt * 2.6666d);
        en.setVelocity(original.add(vel));
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equalsIgnoreCase("Distance"))
        {
            return String.valueOf(lastDistance);
        }
        else if(stat.equalsIgnoreCase("BestDistance"))
        {
            return getBestOrCurrent(p, logic.getName(),"BestDistance", lastDistance);
        }
        return null;
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        List<SQLProperty> list = new List<>();
        list.add(new SQLProperty("Distance", "double", "null", true));
        list.add(new SQLProperty("BestDistance", "double", "0", false));
        return list;
    }
}
