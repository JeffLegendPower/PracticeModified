package me.trixxtraxx.Practice.GameLogic.DuelGameLogic;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DuelGameLogic extends GameLogic
{
    private List<Player> t1 = new ArrayList<>();
    private List<Player> t2 = new ArrayList<>();
    private Game game;
    private Map map;
    private World world;

    public DuelGameLogic(Map m)
    {
        map = m;
    }

    @Override
    public void start(Game gm, List<Player> players)
    {
        if(players.size() % 2 != 0) return;
        game = gm;
        for(int i = 0; i < players.size() / 2; i++)
        {
            t1.add(players.get(i));
            t2.add(players.get(players.size() - 1 - i));
        }

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
    public void stop()
    {
        for (Player p:getPlayers())
        {
            p.teleport(new Location(Bukkit.getWorld("world"),0,100,0));
        }
        map.unload();
        game.stop(false);
    }

    @Override
    public World getWorld() {return world;}

    @Override
    public List<Player> getPlayers()
    {
        List<Player> ppl = new ArrayList<>();
        ppl.addAll(t1);
        ppl.addAll(t2);
        return ppl;
    }

    @Override
    public Game getGame() {return game;}

    @Override
    public Map getMap() {return map;}

    public void loadWorld()
    {
        world = map.load();
    }

    public void everyoneToSpawn()
    {
        for (Player p:getPlayers()) toSpawn(p);
    }

    public void toSpawn(Player p)
    {
        if(triggerEvent(new ToSpawnEvent(p)).isCanceled()) return;
        Location loc = map.getSpawn().getSpawn(this, p);
        p.teleport(loc);
    }
}
