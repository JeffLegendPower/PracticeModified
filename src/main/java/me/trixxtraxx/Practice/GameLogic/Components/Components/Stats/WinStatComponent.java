package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class WinStatComponent extends GameComponent implements IStatComponent
{
    private List<Player> winners = new List<>();
    
    public WinStatComponent(GameLogic logic)
    {
        super(logic);
    }
    
     @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onWin(WinEvent event)
    {
        winners.add(event.getPlayer());
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if (stat.equalsIgnoreCase("Winning"))
        {
            return winners.contains(p) ? "1" : "0";
        }
        else if (stat.equalsIgnoreCase("Wins"))
        {
            return getBestAndAdd(p, logic.getName(), "Wins", winners.contains(p) ? 1 : 0);
        }
        else if (stat.equalsIgnoreCase("WinStreak"))
        {
            if(!winners.contains(p)) return "0";
            return getBestAndAdd(p, logic.getName(), "WinStreak", 1);
        }
        throw new IllegalArgumentException("Stat " + stat + " not found");
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        List<SQLProperty> properties = new List<>();
        properties.add(new SQLProperty("Winning", "int(11)", "0", true));
        properties.add(new SQLProperty("Wins", "int(11)", "0", false));
        properties.add(new SQLProperty("WinStreak", "int(11)", "0", false));
        return properties;
    }
}
