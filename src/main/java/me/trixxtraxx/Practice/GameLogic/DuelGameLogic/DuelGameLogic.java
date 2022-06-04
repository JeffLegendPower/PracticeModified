package me.trixxtraxx.Practice.GameLogic.DuelGameLogic;

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

import me.TrixxTraxx.Linq.List;

public class DuelGameLogic extends GameLogic
{
    protected Player p1;
    protected Player p2;
    protected Game game;
    protected Map map;

    @Override
    public void start(Game gm, List<Player> players, Map m)
    {
        if(players.size()!= 2){
            game.stop(false);
            return;
        }
        map = m;
        game = gm;
        p1 = players.get(0);
        p2 = players.get(1);

        loadWorld();
        GameLogic log = this;
        new BukkitRunnable(){
            @Override
            public void run()
            {
                everyoneToSpawn();
                triggerEvent(new StartEvent(log));
            }
        }.runTaskLater(Practice.Instance,0);
    }

    @Override
    public void stop(boolean force)
    {
        if(triggerEvent(new StopEvent(this, force)).isCanceled() && !force) return;
        Practice.log(3, "Stopping duel game");
        game.stop(false);
        for (Player p:getPlayers())
        {
            BungeeUtil.getInstance().toLobby(p);
        }
        map.unload(false);
    }

    @Override
    public World getWorld() {return map == null ? null : map.getWorld();}

    @Override
    public List<Player> getPlayers()
    {
        return new List<>(p1,p2);
    }

    @Override
    public Game getGame() {return game;}

    @Override
    public Map getMap() {return map;}

    @Override
    public void applyData(String s){}

    public String getData() {return "{}";}
    
    @Override
    public void removePlayer(Player p){remove(p);}
    
    public void loadWorld(){map.load();}

    public Player getP1() {return p1;}
    public Player getP2() {return p2;}

    public void remove(Player p)
    {
        if(p == p1) win(p2);
        if(p == p2) win(p1);
    }

    public void win(Player p)
    {
        if(triggerEvent(new WinEvent(this,p)).isCanceled()) return;
        stop(false);
    }

    public void everyoneToSpawn()
    {
        for (Player p:getPlayers()) toSpawn(p);
    }

    @Override
    public void toSpawn(Player p)
    {
        Location loc = map.getSpawn().getSpawn(this, p);
        if(triggerEvent(new ToSpawnEvent(p, loc)).isCanceled()) return;
        p.teleport(loc);
    }
}