package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Map.Components.AutoScaleComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class SoloAutoscaleLogic extends SoloGameLogic
{
    private int scale = 0;
    
    @Override
    public void start(Game gm, List<Player> players, Map m)
    {
        if(players.size() != 1) return;
        //find the game that is currently running on this gamemode and get its map
        game = gm;
        player = players.get(0);
        GameLogic log = this;
        List<Game> g = Game.getGames().findAll(x -> x.getLogic().getMap() != null && x.getLogic().getMap().getLoad().equalsIgnoreCase(m.getLoad()));
        map = m;
        List<Integer> currentScales = new List<>();
        g.forEach(x -> currentScales.add(((SoloAutoscaleLogic)x.getLogic()).getScale()));
        for(int i = 0; true; i++)
        {
            if(currentScales.contains(i)) continue;
            scale = i;
            break;
        }
        if(g.size() != 0)
        {
            Practice.log(3, "Found " + g.size() + " games on this map, autoscaling...");
            try{
                Field f = map.getClass().getDeclaredField("world");
                f.setAccessible(true);
                f.set(map, g.get(0).getLogic().getWorld());
            }
            catch(NoSuchFieldException | IllegalAccessException e){ e.printStackTrace(); }
            
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    AutoScaleComponent comp = (AutoScaleComponent) map.getComponents(AutoScaleComponent.class).get(0);
                    for(MapComponent mc : map.getComponents())
                    {
                        for(Field f : mc.getClass().getDeclaredFields())
                        {
                            if(f.getType().equals(ConfigLocation.class))
                            {
                                try
                                {
                                    f.setAccessible(true);
                                    ConfigLocation loc = (ConfigLocation) f.get(mc);
                                    ConfigLocation newLoc = comp.convertLoc(loc, scale);
                                    f.set(mc, newLoc);
                                }
                                catch(IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else if(f.getType().equals(Region.class)){
                                try
                                {
                                    f.setAccessible(true);
                                    Region r = (Region) f.get(mc);
                                    Region newR = comp.convertLoc(r, scale);
                                    f.set(mc, newR);
                                }
                                catch(IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
    
                    AutoScaleComponent scale = ((AutoScaleComponent)g.get(0).getLogic().getMap().getComponents(AutoScaleComponent.class).get(0));
                    comp.setStorage(scale);
                    scale.cloneWorld(getScale(), map.getWorld());
                    
                    toSpawn(player);
                    triggerEvent(new StartEvent(log));
                }
            }.runTaskLater(Practice.Instance, 0);
        }
        else
        {
    
            loadWorld();
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    AutoScaleComponent comp = (AutoScaleComponent) map.getComponents(AutoScaleComponent.class).get(0);
                    comp.setStorage(map.getWorld());
                    toSpawn(player);
                    triggerEvent(new StartEvent(log));
                }
            }.runTaskLater(Practice.Instance, 0);
        }
    }
    
    public int getScale()
    {
        return scale;
    }
    
    @Override
    public void stop(boolean dc)
    {
        if(triggerEvent(new StopEvent(this, dc)).isCanceled()) {if(!dc)return;}
        BungeeUtil.getInstance().toLobby(player);
        game.stop(false);
        List<Game> games = Game.getGames().findAll(x -> x.getLogic().getMap() != null && x.getLogic().getWorld() == getWorld());
        if(games.size() == 0) map.unload(false);
        else
        {
            ((AutoScaleComponent)map.getComponents(AutoScaleComponent.class).get(0)).removeScale(getScale());
        }
    }
}
