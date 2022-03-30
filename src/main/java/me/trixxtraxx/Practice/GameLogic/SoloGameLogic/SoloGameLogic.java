package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

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
import java.util.List;

public class SoloGameLogic extends GameLogic
{
    private Player player;
    private Game game;
    private Map map;
    private World world;

    @Override
    public void start(Game gm, List<Player> players, Map m)
    {
        if(players.size() != 1) return;
        map = m;
        game = gm;
        player = players.get(0);

        loadWorld();
        toSpawn(null);
        GameLogic log = this;
        new BukkitRunnable(){
            @Override
            public void run()
            {
                triggerEvent(new StartEvent(log));
            }
        }.runTaskLater(Practice.Instance,0);
    }

    @Override
    public void stop(boolean dc)
    {
        if(triggerEvent(new StopEvent(this, dc)).isCanceled()) {if(!dc)return;}
        player.teleport(new Location(Bukkit.getWorld("world"),0,100,0));
        map.unload(false);
        game.stop(false);
    }

    @Override
    public World getWorld() {return world;}

    @Override
    public List<Player> getPlayers()
    {
        return Collections.singletonList(player);
    }

    @Override
    public Game getGame() {return game;}

    @Override
    public Map getMap() {return map;}

    @Override
    public void applyData(String s)
    {

    }

    public String getData() {return "{}";}

    public Player getPlayer(){return player;}

    public void loadWorld()
    {
        world = map.load();
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