package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
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
    
    @Override
    public String getStat(Player p, String stat)
    {
        return getTicks() + "";
    }
    
    @Override
    public List<IStatComponent.SQLProperty> getSQL()
    {
        List<IStatComponent.SQLProperty> prop = new List<>();
        prop.add(new IStatComponent.SQLProperty("BreakTime", "INT (11) DEFAULT NULL", "null", true));
        return prop;
    }
}
