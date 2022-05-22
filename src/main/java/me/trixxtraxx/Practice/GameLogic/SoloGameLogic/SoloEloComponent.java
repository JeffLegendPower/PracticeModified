package me.trixxtraxx.Practice.GameLogic.SoloGameLogic;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.FFALogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class SoloEloComponent extends GameComponent implements IStatComponent
{
    public SoloEloComponent(GameLogic logic)
    {
        super(logic);
    }
    
    boolean isSet = false;
    boolean success = false;
    
    @TriggerEvent
    public void onReset(ResetEvent event)
    {
        success = event.wasSuccess();
        isSet = true;
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equals("Elo"))
    
        {
            double elo = Double.parseDouble(getBestOrCurrent(p, logic.getName(), stat, 0));
        
            double eloChange = 0;
            if(success) eloChange = (55000 / ((elo * 1.5) + 1000));
            else eloChange = -1 * (((elo * 4) + 1100) / 75);
        
            double newElo = elo + eloChange;
        
            //round newElo and EloChange to 2 decimal places
            newElo = Math.round(newElo * 100.0) / 100.0;
            eloChange = Math.round(eloChange * 100.0) / 100.0;
        
            if(eloChange > 0) p.sendMessage("§9Your new Elo is: §b" + newElo + " (+" + eloChange + ")");
            else p.sendMessage("§9Your new Elo is: §b" + newElo + " (" + eloChange + ")");
        
            return newElo + "";
        }
        throw new IllegalArgumentException("Stat " + stat + " not found");
    }
    
    @Override
    public List<IStatComponent.SQLProperty> getSQL()
    {
        List<IStatComponent.SQLProperty> properties = new List<>();
        properties.add(new IStatComponent.SQLProperty("Elo", "double", "0", false));
        return properties;
    }
}
