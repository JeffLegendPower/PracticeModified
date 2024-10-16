package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import me.TrixxTraxx.Linq.List;

public class SoloGameLogic extends GameLogic
{
    protected Player player;
    protected Game game;
    protected Map map;

    @Override
    public void start(Game gm, List<Player> players, Map m)
    {
        if(players.size() != 1){
            Practice.log(1, "SoloGameLogic had too many players to start, total players: " + players.size());
            for(Player p : players){
                Practice.log(1, "Player: " + p.getName());
            }
            game.stop(false);
            return;
        }
        map = m;
        game = gm;
        player = players.get(0);
        gm.setEndDelay(0);

        loadWorld();
        GameLogic log = this;
        new BukkitRunnable(){
            @Override
            public void run()
            {
                toSpawn(player);
                triggerEvent(new StartEvent(log));
            }
        }.runTaskLater(Practice.Instance,0);
    }

    @Override
    public void stop(boolean dc)
    {
        if(triggerEvent(new StopEvent(this, dc)).isCanceled()) {if(!dc)return;}
        game.stop(false);
    }

    @Override
    public World getWorld() {return map == null ? null : map.getWorld();}

    @Override
    public List<Player> getPlayers()
    {
        return new List<>(player);
    }

    @Override
    public Game getGame() {return game;}

    @Override
    public Map getMap() {return map;}

    @Override
    public void applyData(String s){}
    
    @Override
    public void removePlayer(Player p, boolean force){stop(true);}
    
    public String getData() {return "{}";}

    public Player getPlayer(){return player;}

    public void loadWorld()
    {
        map.load();
    }

    @Override
    public void toSpawn(Player p)
    {
        Location loc = map.getSpawn().getSpawn(this, player);
        if(triggerEvent(new ToSpawnEvent(player, loc)).isCanceled()) return;
        player.teleport(loc);
    }

    public void reset(boolean sucess)
    {
        if(triggerEvent(new ResetEvent(this,sucess)).isCanceled()) return;
        Practice.log(4, "RESETING");
        toSpawn(null);
    }
}