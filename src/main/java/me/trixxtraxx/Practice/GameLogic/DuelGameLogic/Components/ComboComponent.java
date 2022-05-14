package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class ComboComponent extends GameComponent implements IStatComponent
{
    private HashMap<Player, Integer> comboes = new HashMap<>();
    private HashMap<Player, Integer> comboMax = new HashMap<>();
    
    public ComboComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onCombo(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player p = (Player) event.getDamager();
            Player t = (Player) event.getEntity();
            if(!comboes.containsKey(p)) comboes.put(p, 0);
            comboes.put(p, comboes.get(p) + 1);
            
            if(!comboMax.containsKey(p)) comboMax.put(p, 1);
            if(comboes.get(p) > comboMax.get(p)) comboMax.put(p, comboes.get(p));
            
            comboes.remove(t);
        }
    }
    
    @Override
    public String applyPlaceholder(Player p, String s){
        return s.replace("{Combo}", comboes.containsKey(p) ? comboes.get(p).toString() : "0");
    }
    
    @Override
    public String getStat(Player p, String stat)
    {
        if(stat.equals("HighestComboGame"))
        {
            return comboMax.containsKey(p) ? comboMax.get(p).toString() : "0";
        }
        else if(stat.equals("HighestCombo"))
        {
            return getBestOrCurrent(p, logic.getName(), "HighestCombo", comboMax.containsKey(p) ? comboMax.get(p) : 0);
        }
        throw new IllegalArgumentException("Stat " + stat + " does not exist");
    }
    
    @Override
    public List<SQLProperty> getSQL()
    {
        List<SQLProperty> properties = new List<SQLProperty>();
        properties.add(new SQLProperty("HighestComboGame", "int(11)", "0", true));
        properties.add(new SQLProperty("HighestCombo", "int(11)", "0", false));
        return properties;
    }
}
