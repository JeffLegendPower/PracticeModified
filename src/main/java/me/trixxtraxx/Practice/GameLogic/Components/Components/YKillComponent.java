package me.trixxtraxx.Practice.GameLogic.Components.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public class YKillComponent extends GameComponent
{
    @Config
    private int ykillheight;

    public YKillComponent(GameLogic logic, int killheight)
    {
        super(logic);
        ykillheight = killheight;
    }
    public YKillComponent(GameLogic logic){super(logic);}

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
