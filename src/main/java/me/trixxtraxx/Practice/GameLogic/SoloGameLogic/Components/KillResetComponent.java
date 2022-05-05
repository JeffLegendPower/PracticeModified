package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KillResetComponent extends GameComponent
{
    public KillResetComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onDeath(PlayerDeathEvent e)
    {
        Practice.log(4, "Death Reset Triggered!");
        if(logic instanceof SoloGameLogic)
        {
            SoloGameLogic log = (SoloGameLogic) logic;
            if(log.getPlayer() != e.getEntity()) return;
            e.getEntity().setHealth(20);
            //tried 1 and 2 and 10 didnt work :C
            e.getEntity().setNoDamageTicks(20);
            //running it immediately is causing issues with the fake death
            new BukkitRunnable()
            {
                @Override
                public void run() {log.reset(false);}
            }.runTaskLater(Practice.Instance,0);
        }
    }
}