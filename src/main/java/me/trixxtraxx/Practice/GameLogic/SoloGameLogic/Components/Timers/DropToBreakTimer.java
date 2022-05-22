package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import com.google.gson.Gson;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PlayerStats;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class DropToBreakTimer extends TimerComponent implements IStatComponent
{
    @Config
    private Material mat;
    public DropToBreakTimer(GameLogic logic, Material mat)
    {
        super(logic);
        this.mat = mat;
    }
    public DropToBreakTimer(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onDrop(DropEvent e){
        reset();
        start();
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onBlockBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == mat) stop();
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onReset(ResetEvent e){
        stop();
        if(!e.wasSuccess()) reset();
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{" + mat + "Timer}", getTime());
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
        prop.add(new SQLProperty("Break" + mat + "Time", "INT (11) DEFAULT NULL", "null", true));
        prop.add(new SQLProperty("BestBreak" + mat + "Time", "DOUBLE DEFAULT NULL", "null", false));
        return prop;
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equalsIgnoreCase("Break" + mat + "Time")) return getTicks() + "";
        if(stat.equalsIgnoreCase("BestBreak" + mat + "Time")){
            double thisTime = ((double)getTicks()) / 20;
    
            return getWorstOrCurrent(p, logic.getName(),"BestBreak" + mat + "Time", thisTime);
        }
        throw new IllegalArgumentException("Stat " + stat + " not found");
    }
}
