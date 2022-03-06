package me.trixxtraxx.bwp.GameLogic.SoloGameLogic;

import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.Gamemode.Game;
import me.trixxtraxx.bwp.Map.Map;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
    public World getWorld() {return world;}

    public void loadWorld()
    {
        world = map.load();
    }

    public void toSpawn()
    {
        player.teleport(map.getSpawn().getSpawn(this, player));
    }

    public void reset()
    {
        toSpawn();
        resetInventory();
    }

    public void resetInventory()
    {
        game.getKit().setInventory(player);
    }
}