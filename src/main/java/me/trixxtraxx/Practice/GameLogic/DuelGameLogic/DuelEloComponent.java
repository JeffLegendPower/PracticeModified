package me.trixxtraxx.Practice.GameLogic.DuelGameLogic;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class DuelEloComponent extends GameComponent implements IStatComponent
{
    public DuelEloComponent(GameLogic logic)
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
            
            if(!(logic instanceof DuelGameLogic)) return elo + "";
            DuelGameLogic duelLogic = (DuelGameLogic) logic;
            Player opponent = p == duelLogic.getP1() ? duelLogic.getP2() : duelLogic.getP1();
    
            double opponentElo = Double.parseDouble(getBestOrCurrent(opponent, logic.getName(), stat, 0));
    
            double eloDiff = opponentElo - elo;
            double k = 32;
            double expected = 1 / (1 + Math.pow(10, (eloDiff / 400)));
            double eloChange = k * ((isWinner ? 1 : 0) - expected);
            double newElo = elo + eloChange;
            //round newElo and eloChange to 2 decimal places
            newElo = Math.round(newElo * 100.0) / 100.0;
            eloChange = Math.round(eloChange * 100.0) / 100.0;
            if(eloChange > 0) p.sendMessage("§9Your new Elo is: §b" + newElo + " (+" + eloChange + ")");
            else p.sendMessage("§9Your new Elo is: §b" + newElo + " (" + eloChange + ")");
            return newElo + "";
        }
        throw new IllegalArgumentException("Stat " + stat + " not found");
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        List<SQLProperty> properties = new List<>();
        properties.add(new SQLProperty("Elo", "double", "0", false));
        return properties;
    }
}
