package me.trixxtraxx.Practice.Bungee;

import me.trixxtraxx.Practice.SQL.PlayerStats;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.Bukkit;

public class GameAddAction
{
    private String player;
    private String gamemode;
    private PlayerStats.GamemodeGame game;
    
    public GameAddAction(String player, String gamemode, PlayerStats.GamemodeGame game)
    {
        this.player = player;
        this.gamemode = gamemode;
        this.game = game;
    }
    
    public String getPlayer()
    {
        return player;
    }
    
    public String getGamemode()
    {
        return gamemode;
    }
    
    public PlayerStats.GamemodeGame getGame()
    {
        return game;
    }
    
    public void add(){
        PracticePlayer pp = PracticePlayer.getPlayer(Bukkit.getPlayer(player));
        if(pp == null) return;
        
        PlayerStats stats = pp.getStats(gamemode);
        if(stats == null) return;
        
        stats.addGame(game);
    }
}
