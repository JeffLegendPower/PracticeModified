package me.trixxtraxx.bwp.GameLogic.SoloGameLogic;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.bwp.Gamemode.Game;
import me.trixxtraxx.bwp.Map.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SoloGameLogic extends GameLogic
{
    private Player player;
    private Game game;
    private Map map;
    private World world;

    public SoloGameLogic(Map m)
    {
        map = m;
    }

    @Override
    public void start(Game gm, List<Player> players)
    {
        if(players.size() == 0) return;
        game = gm;
        player = players.get(0);

        loadWorld();
        toSpawn();
        resetInventory();
    }

    @Override
    public void stop()
    {
        player.teleport(new Location(Bukkit.getWorld("world"),0,100,0));
        map.unload();
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

    public Player getPlayer(){return player;}

    public void loadWorld()
    {
        world = map.load();
    }

    public void toSpawn()
    {
        if(triggerEvent(new ToSpawnEvent(player)).isCanceled()) return;
        Location loc = map.getSpawn().getSpawn(this, player);
        BWP.log(4, "TELEPORTING TO SPAWN :" + player.getName() + "," + loc);
        player.teleport(loc);
    }

    public void reset()
    {
        if(triggerEvent(new ResetEvent(this)).isCanceled()) return;
        BWP.log(4, "RESETING");
        toSpawn();
        resetInventory();
    }

    public void resetInventory()
    {
        game.getKit().setInventory(player);
    }
}