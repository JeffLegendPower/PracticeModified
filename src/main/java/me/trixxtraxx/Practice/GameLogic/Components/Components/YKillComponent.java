package me.trixxtraxx.Practice.GameLogic.Components.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
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
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(PlayerMoveEvent e)
    {
        if(e.getTo().getY() < ykillheight)
        {
            if(e.getPlayer().getGameMode() == GameMode.CREATIVE)e.getPlayer().setGameMode(GameMode.SURVIVAL);
            e.getPlayer().damage(999999999);
        }
    }
}
