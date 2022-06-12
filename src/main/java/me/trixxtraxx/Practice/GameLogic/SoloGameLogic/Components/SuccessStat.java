package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class SuccessStat extends GameComponent implements IStatComponent
{
    
    public SuccessStat(GameLogic logic)
    {
        super(logic);
    }
    
    private boolean success;
    
    @TriggerEvent
    public void onWin(ResetEvent event)
    {
        success = event.wasSuccess();
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if (stat.equals("success"))
        {
            return success ? 1 + "" : 0 + "";
        }
        throw new IllegalArgumentException("Stat " + stat + " does not exist");
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        return new List<SQLProperty>()
        {
            {
                add(new SQLProperty("success", "tinyint(1)", "0", true));
            }
        };
    }
}
