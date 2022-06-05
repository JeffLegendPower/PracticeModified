package me.trixxtraxx.Practice.Bungee;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class NewGamePacket
{
    private static List<NewGamePacket> packets = new List<NewGamePacket>();
    public enum Type{
        SOLO,
        SOLO_AUTOSCALE,
        DUEL,
        TEAMS,
        FFA,
        FFA_AUTOSCALE
    }
    
    public Type type;
    public String gamemode;
    public String kit;
    public String map;
    public List<String> players;
    public boolean ranked;
    public boolean challenge = false;
    
    private boolean done;
    private Map m;
    private Kit k;
    private GameLogic g;
    private BukkitTask task;
    
    public void init()
    {
        Practice.log(3, "[NewGamePacket] Packet added: " + map + ", " + gamemode + ", " + kit + "," + ranked);
        done = false;
        task = Bukkit.getScheduler().runTaskLater(Practice.Instance, () ->
        {
            if(!done && !update())
            {
                for(String s : this.players)
                {
                    Player p = Bukkit.getPlayer(s);
                    if(p != null){
                        PracticePlayer pp = PracticePlayer.getPlayer(p);
                        if(pp != null){
                            BungeeUtil.getInstance().toLobby(p);
                        }
                    }
                }
            }
            packets.remove(this);
            Practice.log(4, "[NewGamePacket] Packet removed");
        }, 20 * 5);
        packets.add(this);
    
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Map sqlm = SQLUtil.Instance.getMap(map);
                SQLUtil.Instance.applyComponents(sqlm);
                m = sqlm;
                update();
            }
        }.runTaskAsynchronously(Practice.Instance);
        
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Kit sqlk = SQLUtil.Instance.getKit(kit);
                SQLUtil.Instance.applyComponents(sqlk);
                k = sqlk;
                update();
            }
        }.runTaskAsynchronously(Practice.Instance);
        
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                GameLogic sqlgame = SQLUtil.Instance.getLogic(gamemode, ranked);
                SQLUtil.Instance.applyComponents(sqlgame);
                g = sqlgame;
                update();
            }
        }.runTaskAsynchronously(Practice.Instance);
    }
    
    public boolean update()
    {
        if(k == null || m == null || g == null) return false;
        Practice.log(4, "[NewGamePacket] attempting to start game");
        List<Player> players = new List<Player>();
        for(String s : this.players)
        {
            Player p = Bukkit.getPlayer(s);
            if(p == null)
            {
                Practice.log(4, "[NewGamePacket] game start canceled, " + s + " is offline");
                return false;
            }
            players.add(p);
        }
        if(done) return true;
        done = true;
        task.cancel();
        packets.remove(this);
        for(Player p : players)
        {
            for(Lobby l: Lobby.getLobbies())
            {
                l.removePlayer(PracticePlayer.getPlayer(p), false);
            }
        }
        Practice.log(3, "[NewGamePacket] starting game");
        new BukkitRunnable(){
            @Override
            public void run()
            {
                new Game(g, players, k, m, ranked, challenge);
            }
        }.runTask(Practice.Instance);
        return true;
    }
    
    public static NewGamePacket get(Player p){
        for(NewGamePacket n : packets){
            if(n.players.contains(p.getName())){
                return n;
            }
        }
        return null;
    }
    public static void start(List<Player> players, String gamemode, String kit, String map, boolean challenge)
    {
        NewGamePacket n = new NewGamePacket();
        n.type = Type.SOLO;
        n.gamemode = gamemode;
        n.kit = kit;
        n.map = map;
        n.challenge = challenge;
        n.players = new List<>();
        for(Player p : players){
            n.players.add(p.getName());
        }
        n.init();
    }
}