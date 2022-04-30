package me.trixxtraxx.Practice.Bungee;

import me.trixxtraxx.Practice.SQL.PlayerStats;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.Bukkit;

public class GlobalStatUpdateAction
{
    private String player;
    private String gamemode;
    private String stat;
    private String value;
    
    public GlobalStatUpdateAction(String player, String gamemode, String stat, String value)
    {
        this.player = player;
        this.gamemode = gamemode;
        this.stat = stat;
        this.value = value;
    }
    
    public String getPlayer()
    {
        return player;
    }
    
    public String getGamemode()
    {
        return gamemode;
    }
    
    
    public String getStat()
    {
        return stat;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void apply()
    {
        PracticePlayer pp = PracticePlayer.getPlayer(Bukkit.getPlayer(player));
        if(pp == null) return;
        PlayerStats stats = pp.getStats(gamemode);
        
        if(stats == null) return;
        stats.setStat(stat, value);
    }
}
