package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.entity.Player;

public class DropToResetTimer extends TimerComponent implements IStatComponent
{

    public DropToResetTimer(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(GameEvent event)
    {
        if(event instanceof DropEvent) onDrop((DropEvent) event);
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
    }

    public void onDrop(DropEvent e){
        reset();
        start();
    }


    public void onReset(ResetEvent e){
        stop();
        if(!e.wasSuccess()) reset();
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{TotalTimer}", getTime());
    }

    @Override
    public String getStat(Player p) {
        return null;
    }

    @Override
    public String getSQLName() {
        return null;
    }

    @Override
    public String getSQLType() {
        return null;
    }
}
