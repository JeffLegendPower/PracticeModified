package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PlayerStats;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import me.TrixxTraxx.Linq.List;

public interface IStatComponent
{
    public class SQLProperty
    {
        private String name;
        private String type;
        private boolean isPerGame;
        
        public SQLProperty(String name, String type, String defaultValue, boolean isPerGame)
        {
            this.name = name;
            this.type = type;
            this.isPerGame = isPerGame;
        }
        
        public String getName()
        {
            return name;
        }
        
        public String getType()
        {
            return type;
        }
        
        public boolean isPerGame()
        {
            return isPerGame;
        }
    }
    
    public String getStat(Player p, String stat);
    public List<SQLProperty> getSQL();
    
    public default String getBestOrCurrent(Player p, String gm, String stat, double currentValue)
    {
        Practice.log(4, "Getting Stats:" + gm + " " + stat);
        PlayerStats stats = PracticePlayer.getPlayer(p).getStats(gm);
        if(stats == null) return currentValue + "";
        String best = stats.getStat(stat);
        if(best == null || best.isEmpty()) {
            stats.setStat(stat, currentValue + "");
            return currentValue + "";
        }
        double bestd = 0;
        try
        {
            bestd = Double.parseDouble(best);
        }
        catch(Exception e)
        {
            Practice.log(3, "Error parsing double: " + best);
            return currentValue + "";
        }
        Practice.log(4, "Best: " + bestd + " Current: " + currentValue);
        if(currentValue > bestd) {
            stats.setStat(stat, currentValue + "");
            return currentValue + "";
        }
        else return best;
    }
    
    public default String getWorstOrCurrent(Player p, String gm, String stat, double currentValue)
    {
        Practice.log(4, "Getting Stats:" + gm + " " + stat);
        PlayerStats stats = PracticePlayer.getPlayer(p).getStats(gm);
        if(stats == null) return currentValue + "";
        String best = stats.getStat(stat);
        if(best == null || best.isEmpty()) {
            stats.setStat(stat, currentValue + "");
            return currentValue + "";
        }
        double bestd = 0;
        try
        {
            bestd = Double.parseDouble(best);
        }
        catch(Exception e)
        {
            Practice.log(3, "Error parsing double: " + best);
            return currentValue + "";
        }
        Practice.log(4, "Best: " + bestd + " Current: " + currentValue);
        if(currentValue < bestd) {
            stats.setStat(stat, currentValue + "");
            return currentValue + "";
        }
        else return best;
    }
    
    public default String getBestOrNull(Player p, String gm, String stat)
    {
        Practice.log(4, "Getting Stats:" + gm + " " + stat);
        PlayerStats stats = PracticePlayer.getPlayer(p).getStats(gm);
        if(stats == null) return "null";
        String best = stats.getStat(stat);
        if(best == null || best.isEmpty()) return "null";
        double bestd = 0;
        try
        {
            bestd = Double.parseDouble(best);
        }
        catch(Exception e)
        {
            Practice.log(3, "Error parsing double: " + best);
            return "null";
        }
        Practice.log(4, "Value: " + bestd);
        return best;
    }
    
    public default String getWorstOrNull(Player p, String gm, String stat)
    {
        Practice.log(4, "Getting Stats:" + gm + " " + stat);
        PlayerStats stats = PracticePlayer.getPlayer(p).getStats(gm);
        if(stats == null) return "null";
        String best = stats.getStat(stat);
        if(best == null || best.isEmpty()) return "null";
        
        double bestd = 0;
        try
        {
            bestd = Double.parseDouble(best);
        }
        catch(Exception e)
        {
            Practice.log(3, "Error parsing double: " + best);
            return "null";
        }
        Practice.log(4, "Val: " + bestd);
        return best;
    }
    
    public default String getBestAndAdd(Player p, String gm, String stat, double add)
    {
        Practice.log(4, "Getting Stats:" + gm + " " + stat);
        PlayerStats stats = PracticePlayer.getPlayer(p).getStats(gm);
        if(stats == null) return add + "";
        String best = stats.getStat(stat);
        if(best == null || best.isEmpty()) {
            stats.setStat(stat, add + "");
            return add + "";
        }
        double bestd = 0;
        try
        {
            bestd = Double.parseDouble(best);
        }
        catch(Exception e)
        {
            Practice.log(3, "Error parsing double: " + best);
            return add + "";
        }
        Practice.log(4, "Best: " + bestd + " Add: " + add);
        stats.setStat(stat, ((int)(bestd + add)) + "");
        return ((int)(bestd + add)) + "";
    }
}