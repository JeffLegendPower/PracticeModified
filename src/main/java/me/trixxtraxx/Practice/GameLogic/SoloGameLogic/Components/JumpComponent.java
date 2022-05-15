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
