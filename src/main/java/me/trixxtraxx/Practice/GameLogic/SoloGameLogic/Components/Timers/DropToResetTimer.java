package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.entity.Player;

public class DropToResetTimer extends TimerComponent
{

    public DropToResetTimer(GameLogic logic)
    {
        super(logic);
    }
    public DropToResetTimer(GameLogic logic, String s)
    {
        super(logic);
    }
    @Override
    public String getData() {return "";}

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
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{TotalTimer}", getTime());
    }
}
