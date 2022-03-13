package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnProtComponent extends GameComponent
{
    private int spawnProt;
    private String remaining;
    private String starting;

    private HashMap<Player, Location> prot = new HashMap<>();
    private List<Player> tped = new ArrayList<>();

    public SpawnProtComponent(GameLogic logic, int spawnProt, String remaining, String starting)
    {
        super(logic);
        this.spawnProt = spawnProt;
        this.remaining = remaining;
        this.starting = starting;
    }

    @Override
    public void onEvent(GameEvent event){
        if(event instanceof ToSpawnEvent) onSpawn((ToSpawnEvent) event);
    }

    public void onSpawn(ToSpawnEvent e)
    {
        prot.put(e.getPlayer(), e.getLoc());
        new BukkitRunnable(){

            @Override
            public void run()
            {
                prot.remove(e.getPlayer());
            }
        }.runTaskLater(Practice.Instance, spawnProt);
        new BukkitRunnable()
        {
            int left = spawnProt;
            @Override
            public void run()
            {
                if(left == 0)
                {
                        e.getPlayer().sendMessage(logic.applyPlaceholders(e.getPlayer(), starting));
                    cancel();
                    return;
                }
                if(left % 20 == 0)
                {
                    e.getPlayer().sendMessage(logic.applyPlaceholders(e.getPlayer(), remaining.replace("{Timer}", (left / 20) + "")));
                }
                left--;
            }
        }.runTaskTimer(Practice.Instance, 0,1);
    }

    @Override
    public void onEvent(Event event){
        if(event instanceof PlayerMoveEvent) onMove((PlayerMoveEvent) event);
        if(event instanceof EntityDamageEvent) onDamage((EntityDamageEvent) event);
    }

    public void onMove(PlayerMoveEvent e)
    {
        if(prot.get(e.getPlayer()) != null)
        {
            if(e.getTo().getX() == e.getFrom().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ()) return;
            if(tped.contains(e.getPlayer())) return;
            Location loc = prot.get(e.getPlayer());
            tped.add(e.getPlayer());
            e.getPlayer().teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), e.getTo().getYaw(), e.getTo().getPitch()));
            tped.remove(e.getPlayer());
        }
    }

    public void onDamage(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            if(prot.get(e.getEntity()) != null) e.setCancelled(true);
        }
    }
}
