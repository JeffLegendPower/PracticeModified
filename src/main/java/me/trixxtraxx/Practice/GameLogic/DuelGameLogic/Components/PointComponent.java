package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Events.WinEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PointComponent extends GameComponent
{
    @Config
    private int goal;
    @Config
    private String symb;

    private int p1 = 0;
    private int p2 = 0;

    public PointComponent(GameLogic logic, int goal, String symb)
    {
        super(logic);
        this.goal = goal;
        this.symb = symb;
    }
    public PointComponent(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onEvent(WinEvent e)
    {
        if (!(logic instanceof DuelGameLogic)) return;
        DuelGameLogic log = (DuelGameLogic) logic;
        int cur = 0;
        if (log.getP1() == e.getPlayer())
        {
            p1++;
            cur = p1;
        }
        if (log.getP2() == e.getPlayer())
        {
            p2++;
            cur = p2;
        }

        if (cur < goal)
        {
            e.setCanceled(true);
            for (Player p : logic.getPlayers()) logic.toSpawn(p);
        }
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        if (!(logic instanceof DuelGameLogic)) return s;
        String points1 = ChatColor.BLUE + "";
        String points2 = ChatColor.BLUE + "";
        for (int i = 0; i < p1; i++)
        {
            points1 += symb;
        }
        points1 += ChatColor.GRAY;
        for (int i = 0; i < goal - p1; i++)
        {
            points1 += symb;
        }
        for (int i = 0; i < p2; i++)
        {
            points2 += symb;
        }
        points2 += ChatColor.GRAY;
        for (int i = 0; i < goal - p2; i++)
        {
            points2 += symb;
        }
        if (((DuelGameLogic) logic).getP1() == p)
        {
            return s.replace("{Points1}", points1).replace("{Points2}", points2);
        }
        else
        {
            return s.replace("{Points2}", points1).replace("{Points1}", points2);
        }
    }
}