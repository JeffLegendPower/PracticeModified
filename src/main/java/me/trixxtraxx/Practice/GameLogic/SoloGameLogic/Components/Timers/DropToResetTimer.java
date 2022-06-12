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
    private boolean lastWasSuccess = false;
    
    
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
    
    @TriggerEvent(priority = -1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onReset(ResetEvent e)
    {
        stop();
        if(!e.wasSuccess()) reset();
        lastWasSuccess = e.wasSuccess();
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
        prop.add(new SQLProperty("BreakTime", "INT (11)", "null", true));
        prop.add(new SQLProperty("BestBreakTime", "DOUBLE", "null", false));
        return prop;
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equalsIgnoreCase("BreakTime")) return getTicks() + "";
        if(stat.equalsIgnoreCase("BestBreakTime")){
            double thisTime = ((double)getTicks()) / 20;
    
            if(lastWasSuccess) return getWorstOrCurrent(p, logic.getName(),"BestBreakTime", thisTime);
            else return getWorstOrNull(p, logic.getName(),"BestBreakTime");
        }
        throw new IllegalArgumentException("Stat " + stat + " not found");
    }
}
