package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloAutoscaleLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import me.trixxtraxx.Practice.TriggerEvent;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class StatComponent extends GameComponent
{

    public StatComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 999, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(StopEvent event)
    {
        if(!(logic instanceof SoloGameLogic)) store();
    }
    
    @TriggerEvent(priority = 999, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(ResetEvent event)
    {
        if(logic instanceof SoloGameLogic) store();
    }
    
    @TriggerEvent(priority = 999, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(StartEvent event)
    {
        createTable();
    }
    

    public void createTable()
    {
        if(logic.getGame().isChallenge()) return;
        
        //run async
        new BukkitRunnable(){
            @Override
            public void run()
            {
                SQLUtil.Instance.addStatsTable(logic);
            }
        }.runTaskAsynchronously(Practice.Instance);
    }

    public void store()
    {
        if(logic.getGame().isChallenge()) return;
        new BukkitRunnable(){
            @Override
            public void run()
            {
                SQLUtil.Instance.storeStats(logic);
            }
        }.runTaskAsynchronously(Practice.Instance);
    }
}
