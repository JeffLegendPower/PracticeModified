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
        if(players.size() < 2)
        {
            game.stop(false);
            return;
        }
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
    public void stop(boolean force)
    {
        if(game.hasEnded()) return;
        if(triggerEvent(new StopEvent(this, force)).isCanceled()) {if(!force)return;}
        game.stop(false);
    }
    
    @Override
    public World getWorld() {return map == null ? null : map.getWorld();}
    
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
    public void removePlayer(Player p, boolean force)
    {
        if(force)
        {
            players.remove(p);
            BungeeUtil.getInstance().toLobby(p);
            if(players.size() == 1) win(players.first(), true);
        }
        else
        {
            if(game.hasEnded()) return;
            players.remove(p);
            BungeeUtil.getInstance().toLobby(p);
            if(players.size() == 1) win(players.first(), true);
        }
    }
    
    public String getData() {return "{}";}
    
    public void toSpawn(List<Player> p)
    {
        for(Player pl : p)
        {
            toSpawn(pl);
        }
    }
    
    public void win(Player p, boolean force)
    {
        if(game.hasEnded()) return;
        if(triggerEvent(new WinEvent(this, p, force)).isCanceled() && !force) return;
        stop(force);
    }
    
    @Override
    public void toSpawn(Player p)
    {
        Practice.log(4, "Sending player to spawn: " + p.getName());
        if(game.hasEnded()) return;
        Location loc = map.getSpawn().getSpawn(this, p);
        if(triggerEvent(new ToSpawnEvent(p, loc)).isCanceled()) {
            Practice.log(4, "Canceled to spawn event");
            return;
        }
        Practice.log(4, "Now teleporting player to: " + loc.toString());
        p.teleport(loc);
    }
}
