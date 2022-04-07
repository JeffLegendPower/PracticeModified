package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import me.trixxtraxx.Practice.TriggerEvent;

import java.util.ArrayList;
import java.util.List;

public class StatComponent extends GameComponent
{

    public StatComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(GameEvent event)
    {
        if(event instanceof StopEvent)
        {
            if(!(logic instanceof SoloGameLogic)) store();
        }
        if(event instanceof ResetEvent)
        {
            store();
        }
        if(event instanceof StartEvent)
        {
            createTable();
        }
    }

    public void createTable()
    {
        SQLUtil.Instance.addStatsTable(logic);
    }

    public void store()
    {
        SQLUtil.Instance.storeStats(logic);
    }
}
