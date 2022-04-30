package me.trixxtraxx.Practice.GameLogic.FFAGameLogic;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.ToSpawnEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
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
        map = m;
        game = gm;
        this.players = players;
        
        Practice.log(3, "Starting FFA Game");
    
        map.load();
        toSpawn(players);
        GameLogic log = this;
        new BukkitRunnable(){
            @Override
            public void run()
            {
                triggerEvent(new StartEvent(log));
            }
        }.runTaskLater(Practice.Instance, 0);
    }
    
    @Override
    public void stop(boolean dc)
    {
        if(triggerEvent(new StopEvent(this, dc)).isCanceled()) {if(!dc)return;}
        game.stop(false);
        for(Player p : players)
        {
            BungeeUtil.getInstance().toLobby(p);
        }
        map.unload(false);
    }
    
    @Override
    public World getWorld() {return map.getWorld();}
    
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
    public void removePlayer(Player p){stop(true);}
    
    public String getData() {return "{}";}
    
    public void toSpawn(List<Player> p)
    {
        for(Player pl : p)
        {
            toSpawn(pl);
        }
    }
    
    public void win(Player p)
    {
        //TODO SEPERATE MESSAGE INTO ITS OWN COMPONENT
        //send a nice win screen to every player
        String message =
                "§9-------------------------------------\n" +
                "\n" +
                "§b" + p.getName() + "§9 won the Game!\n" +
                "\n" + "- '          &bTop Killers:\n" +
                "§91. {Points1Player}§b {Points1}\n" +
                "§92. {Points2Player}§b {Points2}\n" +
                "§93. {Points3Player}§b {Points3}\n" +
                "\n" +
                "§9-------------------------------------";
        
        applyPlaceholders(p, message);
    }
    
    @Override
    public void toSpawn(Player p)
    {
        Location loc = map.getSpawn().getSpawn(this, p);
        if(triggerEvent(new ToSpawnEvent(p, loc)).isCanceled()) return;
        p.teleport(loc);
    }
}
