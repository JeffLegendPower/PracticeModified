package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameEvents.AllModes.WinEvent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.entity.Player;

public class WinMessageComponent extends GameComponent
{
    @Config
    public String winMessage;
    
    public WinMessageComponent(GameLogic logic, String winMessage)
    {
        super(logic);
        this.winMessage = winMessage;
    }
    
    public WinMessageComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onWin(WinEvent event)
    {
        String msg = logic.applyPlaceholders(event.getPlayer(), winMessage.replace("{Winner}", event.getPlayer().getName()));
        for(Player player : logic.getPlayers())
        {
            player.sendMessage(msg);
        }
    }
}
