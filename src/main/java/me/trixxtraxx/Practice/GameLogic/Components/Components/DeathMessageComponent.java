package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessageComponent extends GameComponent
{
    @Config
    public String deathMessage = "&c{player} &7has been &4killed &7by &c{killer}";
    
    public DeathMessageComponent(GameLogic logic, String msg)
    {
        super(logic);
        deathMessage = msg;
    }
    
    public DeathMessageComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onDeath(PlayerDeathEvent event)
    {
        logic.broadcast(logic.applyPlaceholders(event.getEntity(),
                deathMessage
                .replace("{player}", event.getEntity().getName())
                .replace("{killer}", event.getEntity().getKiller().getName()))
        );
    }
}
