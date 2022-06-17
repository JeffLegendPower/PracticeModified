package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoChestOpenComponent extends GameComponent
{
    public NoChestOpenComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent
    public void onChestOpen(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST)
        {
            event.setCancelled(true);
        }
    }
}
