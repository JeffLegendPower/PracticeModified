package me.trixxtraxx.Practice.Map.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerMoveEvent;

public class KillHeightComponent extends MapComponent
{
    @Config
    private int ykillheight;
    
    public KillHeightComponent(Map m, int killheight)
    {
        super(m);
        ykillheight = killheight;
    }
    public KillHeightComponent(Map m){super(m);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onEvent(PlayerMoveEvent e)
    {
        if(e.getTo().getY() < ykillheight)
        {
            if(e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
            if(e.getPlayer().getGameMode() == GameMode.CREATIVE)e.getPlayer().setGameMode(GameMode.SURVIVAL);
            e.getPlayer().damage(999999999);
        }
    }
    
}
