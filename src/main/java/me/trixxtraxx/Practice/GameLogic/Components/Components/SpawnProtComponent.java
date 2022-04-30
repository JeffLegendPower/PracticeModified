package me.trixxtraxx.Practice.GameLogic.Components.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import me.TrixxTraxx.Linq.List;

public class SpawnProtComponent extends GameComponent
{
    @Config
    protected int spawnProt;
    @Config
    protected String remaining;
    @Config
    protected String starting;

    protected HashMap<Player, Location> prot = new HashMap<>();
    protected List<Player> tped = new List<>();

    public SpawnProtComponent(GameLogic logic, int spawnProt, String remaining, String starting)
    {
        super(logic);
        this.spawnProt = spawnProt;
        this.remaining = remaining;
        this.starting = starting;
    }
    public SpawnProtComponent(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(ToSpawnEvent e)
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
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(PlayerMoveEvent e)
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
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onEvent(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            if(prot.get(e.getEntity()) != null) e.setCancelled(true);
        }
    }
}
