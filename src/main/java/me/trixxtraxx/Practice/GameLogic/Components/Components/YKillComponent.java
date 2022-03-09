package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

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
