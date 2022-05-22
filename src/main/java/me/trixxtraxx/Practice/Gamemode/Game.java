package me.trixxtraxx.Practice.Gamemode;

import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.KitOrderUpdateComponent;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.ChatColor;
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
        try
        {
            kit = k;
            logic = log;
            for(Player p: players)
            {
                PracticePlayer pp = PracticePlayer.getPlayer(p);
                pp.setInQueue(false);
                p.setNoDamageTicks(20);
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setGameMode(GameMode.SURVIVAL);
                p.getEnderChest().clear();
                Game currentGame = getGame(p);
                if(currentGame != null) currentGame.getLogic().removePlayer(p);
            }
            games.add(this);
            logic.start(this, players, m);
            new KitOrderUpdateComponent(logic);
            started = true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            if(log != null) log.stop(true);
            stop(false);
            if(players != null){
                for(Player p : players){
                    p.sendMessage(ChatColor.RED + "Something went wrong");
                }
            }
        }
        BungeeUtil.getInstance().update();
    }

    public GameLogic getLogic() {return logic;}

    public Kit getKit() {return kit;}
    
    public boolean isStarted() {return started;}
    
    public boolean hasEnded() {return ended;}

    public void stop(boolean stopLogic)
    {
        games.remove(this);
        ended = true;
        if (stopLogic) logic.stop(true);
    }

    public static Game getGame(Player p)
    {
        for (Game g : games)
        {
            if (g.getLogic().getPlayers().contains(p)) return g;
        }
        return null;
    }

    public static List<Game> getGames(World w)
    {
        List<Game> gs = new List<>();
        for (Game g : games)
        {
            if (g.getLogic().getWorld() == w) gs.add(g);
        }
        return gs;
    }
    
    public static List<Game> getGames() {return games;}
}