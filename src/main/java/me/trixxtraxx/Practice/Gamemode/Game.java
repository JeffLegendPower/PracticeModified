package me.trixxtraxx.Practice.Gamemode;

import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Map.Map;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private static List<Game> games = new ArrayList<>();
    private GameLogic logic;
    private Kit kit;


    public Game(GameLogic log, List<Player> players, Kit k, Map m)
    {
        games.add(this);
        kit = k;
        logic = log;
        for (Player p : players)
        {
            p.setNoDamageTicks(20);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(20);
            p.setGameMode(GameMode.SURVIVAL);
            p.getEnderChest().clear();
        }
        logic.start(this, players, m);
    }

    public GameLogic getLogic() {return logic;}

    public Kit getKit() {return kit;}

    public void stop(boolean stopLogic)
    {
        if (stopLogic) logic.stop(true);
        games.remove(this);
    }

    public static Game getGame(Player p)
    {
        for (Game g : games)
        {
            if (g.getLogic().getPlayers().contains(p)) return g;
        }
        return null;
    }

    public static Game getGame(World w)
    {
        for (Game g : games)
        {
            if (g.getLogic().getWorld() == w) return g;
        }
        return null;
    }
}