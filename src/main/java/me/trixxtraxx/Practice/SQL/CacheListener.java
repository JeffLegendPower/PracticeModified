package me.trixxtraxx.Practice.SQL;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CacheListener implements Listener
{
    private static List<PracticePlayer> cached = new List<>();
    
    public static void add(PracticePlayer player)
    {
        cached.add(player);
        player.resetKit();
        //remove after 5s
        Practice.Instance.getServer().getScheduler().runTaskLater(Practice.Instance, () -> cached.remove(player), 100);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e)
    {
        PracticePlayer pp = cached.find(x -> x.getName().contentEquals(e.getPlayer().getName()));
        if(pp == null)
        {
            Practice.log(1, "Player " + e.getPlayer().getName() + " not found in cache");
            e.getPlayer().kickPlayer("§cSomething went wrong (no data forwarded by bungeecord)");
        }
        else
        {
            PracticePlayer.add(pp);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        PracticePlayer.remove(PracticePlayer.getPlayer(e.getPlayer()));
    }
}