package me.trixxtraxx.Practice.SQL;

import me.TrixxTraxx.Linq.List;

public class PlayerStats
{
    private String Gamemode;
    private List<GamemodeStat> stats;
    private List<GamemodeGame> games;
    
    public static class GamemodeStat
    {
        private String Name;
        private String Value;
        
        public GamemodeStat(String name, Object value)
        {
            this.Name = name;
            this.Value = String.valueOf(value);
        }
        
        public String getName()
        {
            return this.Name;
        }
        
        public String getValue()
        {
            return this.Value;
        }
    }
    
    public static class GamemodeGame
    {
        List<GamemodeStat> stats = new List();
        
        public GamemodeGame(List<GamemodeStat> stats)
        {
            this.stats = stats;
        }
        
        public List<GamemodeStat> getStats()
        {
            return this.stats;
        }
        
        public String getStat(String name)
        {
            return stats.find(x -> x.Name.equalsIgnoreCase(name)).Value;
        }
    }
    
    public PlayerStats(String gamemode, List<GamemodeStat> stats, List<GamemodeGame> games)
    {
        this.Gamemode = gamemode;
        this.stats = stats;
        this.games = games;
    }
    
    public List<GamemodeStat> getStats()
    {
        return this.stats;
    }
    
    public String getStat(String name)
    {
        return stats.find(x -> x.Name.equalsIgnoreCase(name)).Value;
    }
    
    public List<GamemodeGame> getGames()
    {
        return this.games;
    }
    
    public String getGamemode()
    {
        return this.Gamemode;
    }
}
