package me.trixxtraxx.Practice.Gamemode;

import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Map.Map;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;

public class Game
{
    private static List<Game> games = new List<>();
    private GameLogic logic;
    private Kit kit;
    
    private boolean started = false;
    private boolean ended = false;

    public Game(GameLogic log, List<Player> players, Kit k, Map m)
    {
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
        games.add(this);
        logic.start(this, players, m);
        started = true;
        BungeeUtil.getInstance().update();
    }

    public GameLogic getLogic() {return logic;}

    public Kit getKit() {return kit;}
    
    public boolean isStarted() {return started;}
    
    public boolean hasEnded() {return ended;}

    public void stop(boolean stopLogic)
    {
        if (stopLogic) logic.stop(true);
        games.remove(this);
        ended = true;
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
    
    public static List<Game> getGames() {return games;}
}