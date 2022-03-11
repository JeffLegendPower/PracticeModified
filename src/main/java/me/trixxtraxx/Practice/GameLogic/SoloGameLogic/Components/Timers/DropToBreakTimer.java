package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class DropToBreakTimer extends TimerComponent
{
    private Material mat;
    public DropToBreakTimer(GameLogic logic, Material mat)
    {
        super(logic);
        this.mat = mat;
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof BlockBreakEvent) onBlockBreak((BlockBreakEvent) event);
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

    public void onBlockBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == mat) stop();
    }

    public void onReset(ResetEvent e){
        stop();
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{" + mat + "Timer}", getTime());
    }
}
