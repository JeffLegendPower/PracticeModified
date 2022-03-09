package me.trixxtraxx.Practice.GameLogic.Components.Components.Timer;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class TimerComponent extends GameComponent
{
    public TimerComponent(GameLogic logic)
    {
        super(logic);
    }

    private int time = 0;
    private BukkitRunnable run;

    public void start()
    {
        run = new BukkitRunnable() {
            @Override
            public void run()
            {
                time++;
            }
        };
        run.runTaskTimer(Practice.Instance,0,1);
    }

    public void stop()
    {
        if(run != null) run.cancel();
    }

    public void reset()
    {
        time = 0;
    }

    public String getTime()
    {
        return (((double)Math.round(time / 2d)) / 10d) + "s";
    }

    public int getTicks(){return time;}
}
