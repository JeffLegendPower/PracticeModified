package me.trixxtraxx.bwp.GameLogic.Components.Components;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class YKillComponent extends GameComponent
{
    private int ykillheight;

    public YKillComponent(GameLogic logic, int killheight)
    {
        super(logic);
        ykillheight = killheight;
    }

    @Override
    public void onEvent(Event event){
        if(event instanceof PlayerMoveEvent) onMove((PlayerMoveEvent) event);
    }

    public void onMove(PlayerMoveEvent e)
    {
        if(e.getTo().getY() < ykillheight)
        {
            if(e.getPlayer().getGameMode() == GameMode.CREATIVE)e.getPlayer().setGameMode(GameMode.SURVIVAL);
            e.getPlayer().damage(999999999);
        }
    }
}
