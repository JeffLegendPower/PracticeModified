package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.ResetHealComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class BridgeEggComponent extends GameComponent
{
    public BridgeEggComponent(GameLogic logic)
    {
        super(logic);
    }
    
    private HashMap<Integer, BukkitRunnable> runnables = new HashMap<>();
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void OnProjectileThrow(ProjectileLaunchEvent e)
    {
        if(e.getEntity() instanceof Egg)
        {
            if(e.getEntity().getShooter() instanceof Player)
            {
                Player p = (Player) e.getEntity().getShooter();
                BukkitRunnable run = new BukkitRunnable()
                {
                    int iteration = 0;
                    List<List<Location>> locs = new List<>();
                    Location lastLoc;
                    @Override
                    public void run()
                    {
                        iteration++;
                        if(iteration > 40)
                        {
                            runnables.remove(e.getEntity().getEntityId());
                            cancel();
                            return;
                        }
                        
                        if(lastLoc != null)
                        {
                            List<Location> newlocs = new List<>();
                            
                            Location newLoc = e.getEntity().getLocation();
                            
                            int dist = (int) lastLoc.distance(newLoc);
                            if(dist == 0) dist = 1;
                            BlockIterator iterator = new BlockIterator(lastLoc.getWorld(), lastLoc.toVector(), genVec(lastLoc, newLoc), 0, dist );
                            int blocks = 0;
                            while (iterator.hasNext())
                            {
                                blocks++;
                                if(blocks > 100){
                                    break;
                                }
                                Location loc = iterator.next().getLocation();
                                newlocs.add(loc);
                            }
                            locs.add(newlocs);
                        }
                        
                        if(iteration > 2)
                        {
                            for (Location loc:locs.get(iteration - 3))
                            {
                                Block b = loc.getBlock();
                                b.setType(Material.WOOL);
                            }
                        }
                        lastLoc = e.getEntity().getLocation();
                    }
                };
                runnables.put(e.getEntity().getEntityId(), run);
                run.runTaskTimer(Practice.Instance, 4, 1);
            }
        }
    }
    
    public static Vector genVec(Location a, Location b)
    {
        double dX = a.getX() - b.getX();
        double dY = a.getY() - b.getY();
        double dZ = a.getZ() - b.getZ();
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);
        
        Vector vector = new Vector(x, z, y);
        //If you want to: vector = vector.normalize();
        
        return vector;
    }
    
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onProjHit(ProjectileHitEvent e)
    {
        BukkitRunnable run = runnables.get(e.getEntity().getEntityId());
        runnables.remove(e.getEntity().getEntityId());
        if(run == null) return;
        run.cancel();
    }
    
}
