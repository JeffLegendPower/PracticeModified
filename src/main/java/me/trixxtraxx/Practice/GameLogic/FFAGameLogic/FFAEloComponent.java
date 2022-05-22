package me.trixxtraxx.Practice.GameLogic.FFAGameLogic;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class FFAEloComponent extends GameComponent implements IStatComponent
{
    public FFAEloComponent(GameLogic logic)
    {
        super(logic);
    }
    
    private Player winner;
    
    @TriggerEvent
    public void onPlayerWin(WinEvent event)
    {
        winner = event.getPlayer();
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equals("Elo"))
        {
            double elo = Double.parseDouble(getBestOrCurrent(p, logic.getName(), stat, 0));
            boolean isWinner = winner == p;
            
            if(!(logic instanceof FFALogic)) return elo + "";
            FFALogic ffaLogic = (FFALogic) logic;
            
            double winChance = 1 / ffaLogic.getPlayers().size();
            
            double eloChange = 0;
            if(isWinner) eloChange = (55000 / ((elo * 1.5 ) + 1000));
            else eloChange = -1 * ( ( (elo * 4) + 1100 ) / 200 ) * (winChance * 8);
            
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
