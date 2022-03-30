package me.trixxtraxx.Practice.Kit.Editor;

import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class KitEditor
{
    private static KitEditor instance;
    private Region region;
    private List<Player> players;

    private KitEditor(Region r){region = r;}
    public static void init(Region r)
    {
        if(hasInstance()) return;
        instance = new KitEditor(r);
    }
    public static boolean hasInstance(){return instance != null;}
    public static KitEditor getInstance(){return instance;}

    public void playerMove(PlayerMoveEvent e)
    {
        if (region.contains(e.getTo()))
        {
            if (!players.contains(e.getPlayer())) enter(e.getPlayer());
            else remove(e.getPlayer());
        }
    }

    public void enter(Player p)
    {
        players.add(p);
        setInventory(p);
        p.sendMessage("§9You are now in the kit editing area!");
        p.sendMessage("§bYou can edit your kit here, you can save it by exiting the area");
    }

    public void remove(Player p)
    {
        players.remove(p);
        PracticePlayer pp = PracticePlayer.getPlayer(p);
        Kit k = pp.getKit();
        k.save(pp);
        p.getInventory().clear();
    }

    public void setInventory(Player p)
    {
        p.getInventory().clear();
        PracticePlayer pp = PracticePlayer.getPlayer(p);
        Kit k = pp.getKit();
        k.setInventory(p);
    }
}