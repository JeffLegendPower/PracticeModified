package me.trixxtraxx.Practice.GameLogic.Components.Components.Stats;

import me.trixxtraxx.Practice.GameEvents.AllModes.StopEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;

import java.util.List;

public class StatComponent extends GameComponent
{

    public StatComponent(GameLogic logic)
    {
        super(logic);
    }
    public StatComponent(GameLogic logic, String data)
    {
        super(logic);
    }
    @Override
    public String getData() {return "{}";}

    @Override
    public void onEvent(GameEvent event){
        if(event instanceof StopEvent)
        {
            if(!(logic instanceof SoloGameLogic)) store();
        }
        if(event instanceof ResetEvent)
        {
            store();
        }
    }

    public void store()
    {
        List<GameComponent> comps = logic.getComponents(StatCountingComponent.class);

    }
}
