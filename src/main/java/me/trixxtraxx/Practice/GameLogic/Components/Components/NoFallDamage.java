package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoFallDamage extends GameComponent
{
    public NoFallDamage(GameLogic logic)
    {
        super(logic);
    }
    public NoFallDamage(GameLogic logic, String s)
    {
        super(logic);
    }
    @Override
    public String getData() {return "";}

    @Override
    public void onEvent(Event event){
        if(event instanceof EntityDamageEvent) onDmg((EntityDamageEvent) event);
    }

    public void onDmg(EntityDamageEvent e){if(e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);}
}