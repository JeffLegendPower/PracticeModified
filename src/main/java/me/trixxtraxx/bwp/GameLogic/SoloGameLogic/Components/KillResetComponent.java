package me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.SoloGameLogic;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KillResetComponent extends GameComponent
{
    public KillResetComponent(GameLogic logic)
    {
        super(logic);
    }

    @Override
    public void onEvent(Event event){
        if(event instanceof PlayerDeathEvent) onDeath((PlayerDeathEvent) event);
    }

    public void onDeath(PlayerDeathEvent e)
    {
        if(logic instanceof SoloGameLogic)
        {
            SoloGameLogic log = (SoloGameLogic) logic;
            if(log.getPlayer() != e.getEntity()) return;
            e.getEntity().setHealth(20);
            new BukkitRunnable()
            {
                @Override
                public void run() {log.reset();}
            }.runTaskLater(BWP.Instance,0);
        }
    }
}