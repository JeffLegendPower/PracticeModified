package me.trixxtraxx.Practice.Kit.Editor;

import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;

public class KitEditor
{
    private static KitEditor instance;
    private Region region;
    private List<Player> players = new List<>();
    private String world;

    private KitEditor(Region r, String w)
    {
        region = r;
        world = w;
        Practice.log(4, "Kit Editor Created:\n" + r.getLocation1(Bukkit.getWorld("world")) + "\n" + r.getLocation2(Bukkit.getWorld("world")));
    }
    public static void init(Region r, String w)
    {
        if(hasInstance()) return;
        instance = new KitEditor(r,w);
    }
    public static boolean hasInstance(){return instance != null;}
    public static KitEditor getInstance(){return instance;}

    public void playerMove(PlayerMoveEvent e)
    {
        if(!e.getTo().getWorld().getName().contentEquals(world)) return;
        if (region.contains(e.getTo()))
        {
            if (!players.contains(e.getPlayer()))
            {
                enter(e.getPlayer());
            }
        }
        else
        {
            if (players.contains(e.getPlayer()))
            {
                remove(e.getPlayer());
            }
        }
    }

    public void enter(Player p)
    {
        players.add(p);
        setInventory(p);
        p.setGameMode(GameMode.CREATIVE);
        p.setAllowFlight(false);
        p.sendMessage("§9You are now in the kit editing area!");
        p.sendMessage("§bYou can edit your kit here, you can save it by exiting the area");
    }

    public void remove(Player p)
    {
        players.remove(p);
        PracticePlayer pp = PracticePlayer.getPlayer(p);
        pp.saveKit();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setGameMode(GameMode.SURVIVAL);
        p.getActivePotionEffects().forEach(x -> p.removePotionEffect(x.getType()));
        p.sendMessage("§9You are no longer in the kit editing area!");
        p.sendMessage("§bYour kit has been saved!");
        Lobby l = Lobby.get(Bukkit.getWorld("world"));
        if(l != null){
            l.setInv(pp);
        }
    }

    public void setInventory(Player p)
    {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        PracticePlayer pp = PracticePlayer.getPlayer(p);
        Kit k = pp.getKit();
        if(k != null) k.setInventory(p, false);
    }
    
    public boolean hasPlayer(Player p)
    {
        return players.contains(p);
    }
    
    public void saveAll(){
        for(Player p : players){
            PracticePlayer pp = PracticePlayer.getPlayer(p);
            pp.saveKit();
        }
    }
}