package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Events.WinEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PointComponent extends GameComponent
{
    private Settings settings = new Settings();
    private class Settings
    {
        private int goal;
        private String symb;
    }

    private int p1 = 0;
    private int p2 = 0;

    public PointComponent(GameLogic logic, int goal, String symb)
    {
        super(logic);
        settings.goal = goal;
        settings.symb = symb;
    }
    public PointComponent(GameLogic logic, String s)
    {
        super(logic);
        settings = new Gson().fromJson(s, Settings.class);
    }
    @Override
    public String getData() {return new Gson().toJson(settings);}

    @Override
    public void onEvent(GameEvent e)
    {
        if(e instanceof WinEvent) onWin((WinEvent) e);
    }

    public void onWin(WinEvent e)
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

        if (cur < settings.goal)
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
            points1 += settings.symb;
        }
        points1 += ChatColor.GRAY;
        for (int i = 0; i < settings.goal - p1; i++)
        {
            points1 += settings.symb;
        }
        for (int i = 0; i < p2; i++)
        {
            points2 += settings.symb;
        }
        points2 += ChatColor.GRAY;
        for (int i = 0; i < settings.goal - p2; i++)
        {
            points2 += settings.symb;
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