package me.trixxtraxx.Practice.Bungee;

import me.TrixxTraxx.Linq.List;
import me.TrixxTraxx.RestCommunicator.PluginAPI.IMessageReceived;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SpectateListener implements IMessageReceived
{
    private static List<SpecRequest> specs = new List<>();
    
    private static class SpecRequest
    {
        public String player;
        public String target;
        public BukkitRunnable run;
        
        public SpecRequest(String player, String target)
        {
            this.player = player;
            this.target = target;
        }
    }
    
    @Override
    public boolean IsChannel(String s)
    {
        return s.equalsIgnoreCase("Practice_Spectate_Join");
    }
    
    @Override
    public void MessageReceived(String s)
    {
        String[] split = s.split("<>");
        
        SpecRequest req = new SpecRequest(split[0], split[1]);
        
        specs.add(req);
        refresh(split[1]);
    
        new BukkitRunnable(){
            @Override
            public void run()
            {
                specs.remove(req);
            }
        }.runTaskLater(Practice.Instance, 20 * 5);
    }
    
    public void refresh(String pl)
    {
        SpecRequest spec = specs.find(x -> x.player.equalsIgnoreCase(pl));
        if(spec == null) return;
        
        Player p = Bukkit.getPlayer(spec.player);
        Player t = Bukkit.getPlayer(spec.target);
        
        if(p == null || t == null || !p.isOnline()) return;
        
        Game g = Game.getGame(t);
        
        //run g.addSpectator(p); sync
        new BukkitRunnable(){
            @Override
            public void run()
            {
                g.addSpectator(p);
            }
        }.runTask(Practice.Instance);
        
        specs.remove(spec);
    }
}
