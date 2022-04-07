package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Timer.TimerComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class DropToBreakTimer extends TimerComponent implements IStatComponent
{
    private Material mat;
    public DropToBreakTimer(GameLogic logic, Material mat)
    {
        super(logic);
        this.mat = mat;
    }
    public DropToBreakTimer(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onDrop(DropEvent e){
        reset();
        start();
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onBlockBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == mat) stop();
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onReset(ResetEvent e){
        stop();
        if(!e.wasSuccess()) reset();
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        return s.replace("{" + mat + "Timer}", getTime());
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
