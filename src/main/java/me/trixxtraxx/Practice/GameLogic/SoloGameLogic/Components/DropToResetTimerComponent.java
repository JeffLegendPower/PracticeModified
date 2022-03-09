package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class DropToResetTimerComponent extends TimerComponent
{

    public DropToResetTimerComponent(GameLogic logic)
    {
        super(logic);
    }

    public void onEvent(GameEvent event){
        if(event instanceof DropEvent) onDrop((DropEvent) event);
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
    }

    public void onDrop(DropEvent e){
        start();
    }

    public void onReset(ResetEvent e){
        stop();
        for (Player p:logic.getPlayers())
        {
            p.sendTitle(ChatColor.GREEN + "SUCESS","Time: " + getTime());
        }
        reset();
    }


}
