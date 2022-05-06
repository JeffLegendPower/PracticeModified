package me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PointComponent extends GameComponent
{
    @Config
    private int goal;
    @Config
    private String symb;
    @Config
    boolean onWin = true;
    @Config
    boolean onHit = true;

    private int p1 = 0;
    private int p2 = 0;

    public PointComponent(GameLogic logic, int goal, String symb, boolean onWin, boolean onHit)
    {
        super(logic);
        this.goal = goal;
        this.symb = symb;
    }
    public PointComponent(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(WinEvent e)
    {
        if(!onWin) return;
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

    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(EntityDamageByEntityEvent e)
    {
        if(!onHit) return;
        if (!(logic instanceof DuelGameLogic)) return;
        DuelGameLogic log = (DuelGameLogic) logic;
        if(!(e.getEntity() instanceof Player)) return;
        Player p2 = null;
        if(e.getDamager() instanceof Player)
        {
            p2 = (Player) e.getDamager();
        }
        else if(e.getDamager() instanceof Projectile)
        {
            Projectile proj = (org.bukkit.entity.Projectile) e.getDamager();
            if(proj.getShooter() instanceof Player)
            {
                p2 = (Player) proj.getShooter();
            }
        }
        else return;
        if(log.getP1() == p2) this.p1++;
        if(log.getP2() == p2) this.p2++;
    }

    @Override
    public String applyPlaceholder(Player p, String s)
    {
        if (!(logic instanceof DuelGameLogic)) return s;
        if(goal > 5)
        {
            return s.replace("{Points1}", p1 + "").replace("{Points2}", p2 + "");
        }
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