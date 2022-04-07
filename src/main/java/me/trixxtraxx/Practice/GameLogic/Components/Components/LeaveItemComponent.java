package me.trixxtraxx.Practice.GameLogic.Components.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeaveItemComponent extends GameComponent
{
    private Material mat;
    public LeaveItemComponent(GameLogic logic, Material mat)
    {
        super(logic);
        this.mat = mat;
    }
    public LeaveItemComponent(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onEvent(PlayerInteractEvent e)
    {
        if(e.getItem() != null && e.getItem().getType() == mat)
        {
            e.setCancelled(true);
            e.getPlayer().performCommand("lobby");
        }
    }
}