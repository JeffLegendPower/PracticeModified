package me.trixxtraxx.Practice.Gamemode;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.KitOrderUpdateComponent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloAutoscaleLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Map.Components.AutoScaleComponent;
import me.trixxtraxx.Practice.Map.Components.SpectatorSpawnComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;
import org.bukkit.scheduler.BukkitRunnable;

public class Game
{
    private static List<Game> games = new List<>();
    private GameLogic logic;
    private Kit kit;
    private List<Player> spectators = new List<>();
    
    private boolean started = false;
    private boolean ended = false;
    private boolean ranked = false;
    private boolean challenge = false;
    private long startTime;
    private int endDelay = 60;

    public Game(GameLogic log, List<Player> players, Kit k, Map m, boolean ranked, boolean challenge)
    {
        try
        {
            startTime = System.currentTimeMillis();
            kit = k;
            logic = log;
            this.ranked = ranked;
            this.challenge = challenge;
            for(Player p: players)
            {
                PracticePlayer pp = PracticePlayer.getPlayer(p);
                pp.setInQueue(false);
                p.setNoDamageTicks(20);
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setGameMode(GameMode.SURVIVAL);
                p.getEnderChest().clear();
                p.setMaximumNoDamageTicks(20);
                
                Game currentGame = getGame(p);
                if(currentGame != null) currentGame.getLogic().removePlayer(p, true);
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
    public void setKit(Kit kit) {this.kit = kit;}
    
    public void addSpectator(Player p)
    {
        spectators.add(p);
        
        List<MapComponent> comps = logic.getMap().getComponents(SpectatorSpawnComponent.class);
        Location spawn = null;
        if(comps.size() == 0) spawn = new Location(logic.getWorld(), 0,100,0);
        else spawn = ((SpectatorSpawnComponent)comps.get(0)).loc.getLocation(logic.getWorld());
        
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(spawn);
    }
    
    public boolean isStarted() {return started;}
    
    public boolean hasEnded() {return ended;}
    
    public boolean isRanked() {return ranked;}
    
    public boolean isChallenge() {return challenge;}
    
    public long getStartTime() {return startTime;}
    public int getEndDelay() {return endDelay;}
    public void setEndDelay(int endDelay) {this.endDelay = endDelay;}

    public void stop(boolean stopLogic)
    {
        if(ended) return;
        games.remove(this);
        ended = true;
    
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                
                for(Player p: logic.getPlayers())
                {
                    if(p.getWorld() == logic.getWorld())
                    {
                        p.setHealth(20);
                        BungeeUtil.getInstance().toLobby(p);
                    }
                }
    
                //delay 1 tick to make sure all players are gone and the world can be unloaded
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Map map = logic.getMap();
                        if(logic instanceof SoloAutoscaleLogic)
                        {
                            SoloAutoscaleLogic sl = (SoloAutoscaleLogic)logic;
                            List<Game> games = Game.getGames().findAll(x -> x.getLogic().getMap() != null && x.getLogic().getWorld() == sl.getWorld());
                            if(games.size() == 0) map.unload(false);
                            else
                            {
                                ((AutoScaleComponent)map.getComponents(AutoScaleComponent.class).get(0)).removeScale(sl.getScale());
                            }
                        }
                        else
                        {
                            map.unload(false);
                        }
                    }
                }.runTaskLater(Practice.Instance, 2);
                
                
            }
        }.runTaskLater(Practice.Instance, endDelay);
    
        if(stopLogic) logic.stop(true);
    }

    public static Game getGame(Player p)
    {
        for (Game g : games)
        {
            if(g.getLogic().getMap() == null) {
                g.stop(true);
                continue;
            }
            if (g.getLogic().getPlayers().contains(p)) return g;
        }
        return null;
    }

    public static List<Game> getGames(World w)
    {
        List<Game> gs = new List<>();
        for (Game g : games)
        {
            if(g.getLogic().getMap() == null) {
                g.stop(true);
                continue;
            }
            if (g.getLogic().getWorld() == w) gs.add(g);
        }
        return gs;
    }
    
    public static List<Game> getGames() {return games;}
}