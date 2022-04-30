package me.trixxtraxx.Practice.GameLogic.FFAGameLogic;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FFALogic extends GameLogic
{
    private List<Player> players;
    private Game game;
    private Map map;
    
    @Override
    public void start(Game gm, List<Player> players, Map m)
    {
        if(players.size() == 1) return;
        map = m;
        game = gm;
        this.players = players;
        
        Practice.log(3, "Starting FFA Game");
    
        map.load();
        GameLogic log = this;
        new BukkitRunnable(){
            @Override
            public void run()
            {
                toSpawn(players);
                triggerEvent(new StartEvent(log));
            }
        }.runTaskLater(Practice.Instance, 0);
    }
    
    @Override
    public void stop(boolean dc)
    {
        if(game.hasEnded()) return;
        if(triggerEvent(new StopEvent(this, dc)).isCanceled()) {if(!dc)return;}
        game.stop(false);
        for(Player p : players)
        {
            BungeeUtil.getInstance().toLobby(p);
        }
        //delay 1 tick to make sure all players are gone and the world can be unloaded
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                map.unload(false);
            }
        }.runTaskLater(Practice.Instance, 1);
    }
    
    @Override
    public World getWorld() {return map.getWorld();}
    
    @Override
    public List<Player> getPlayers()
    {
        return players;
    }
    
    @Override
    public Game getGame() {return game;}
    
    @Override
    public Map getMap() {return map;}
    
    @Override
    public void applyData(String s){}
    
    @Override
    public void removePlayer(Player p)
    {
        if(game.hasEnded()) return;
        players.remove(p);
        if(players.size() == 1) win(players.first(), true);
    }
    
    public String getData() {return "{}";}
    
    public void toSpawn(List<Player> p)
    {
        for(Player pl : p)
        {
            toSpawn(pl);
        }
    }
    
    public void win(Player p, boolean dc)
    {
        if(game.hasEnded()) return;
        if(triggerEvent(new WinEvent(this, p)).isCanceled() && !dc) return;
        stop(dc);
    }
    
    @Override
    public void toSpawn(Player p)
    {
        if(game.hasEnded()) return;
        Location loc = map.getSpawn().getSpawn(this, p);
        if(triggerEvent(new ToSpawnEvent(p, loc)).isCanceled()) return;
        p.teleport(loc);
    }
}
