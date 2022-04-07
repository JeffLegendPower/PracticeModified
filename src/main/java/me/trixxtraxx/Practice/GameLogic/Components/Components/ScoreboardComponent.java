package me.trixxtraxx.Practice.GameLogic.Components.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.AllModes.StartEvent;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.libs.Fastboard.FastBoard;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardComponent extends GameComponent
{
    @Config
    private String title;
    @Config
    private List<String> lines;

    private BukkitRunnable run;
    private HashMap<Player, FastBoard> boards = new HashMap<>();

    public ScoreboardComponent(GameLogic logic, String title, List<String> lines)
    {
        super(logic);
        title = title;
        lines = lines;
    }
    public ScoreboardComponent(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(StartEvent e)
    {
        if(boards.size() != 0) return;
        for (Player p:e.getlogic().getPlayers())
        {
            FastBoard board = new FastBoard(p);
            board.updateTitle(e.getlogic().applyPlaceholders(p,title));
            board.updateLines(e.getlogic().applyPlaceholders(p,lines));
            boards.put(p, board);
        }
        run = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Map.Entry<Player, FastBoard> entry:boards.entrySet())
                {
                    entry.getValue().updateTitle(e.getlogic().applyPlaceholders(entry.getKey(),title));
                    entry.getValue().updateLines(e.getlogic().applyPlaceholders(entry.getKey(),lines));
                }
            }
        };
        run.runTaskTimer(Practice.Instance, 0, 1);
    }
}
