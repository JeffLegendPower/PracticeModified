package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PlayerStats;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class DropToResetTimer extends TimerComponent implements IStatComponent
{
    
    public DropToResetTimer(GameLogic logic)
    {
        super(logic);
    }
    
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onDrop(DropEvent e)
    {
        reset();
        start();
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onReset(ResetEvent e)
    {
        stop();
        if(!e.wasSuccess()) reset();
    }
    
    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{TotalTimer}", getTime());
    }
    
    boolean success = false;
    
    @TriggerEvent
    public void onEnd(ResetEvent e){
        success = e.wasSuccess();
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        List<SQLProperty> prop = new List<>();
        prop.add(new SQLProperty("BlockinTime", "INT (11) DEFAULT NULL", "null", true));
        prop.add(new SQLProperty("BestBlockinTime", "DOUBLE DEFAULT NULL", "null", false));
        return prop;
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equalsIgnoreCase("BlockinTime")) return getTicks() + "";
        if(stat.equalsIgnoreCase("BestBlockinTime")){
            double thisTime = ((double)getTicks()) / 20;
            
            PracticePlayer pp = PracticePlayer.getPlayer(p);
            if(pp == null) return success ? thisTime + "" : "NULL";
            
            PlayerStats stats = pp.getStats(logic.getName());
            if(stats == null) return success ? thisTime + "" : "NULL";
            
            String bestString = stats.getStat("BestBlockinTime");
            if(!success) return bestString;
            if(bestString == null || bestString.equalsIgnoreCase("null") || bestString.isEmpty()) return thisTime + "";
            
            double bestTime = Double.parseDouble(bestString);
            Practice.log(4, "Best time: " + bestTime + " This time: " + thisTime);
            if(thisTime < bestTime) return thisTime + "";
            else return bestTime + "";
        }
        throw new IllegalArgumentException("Stat " + stat + " not found");
    }
}
