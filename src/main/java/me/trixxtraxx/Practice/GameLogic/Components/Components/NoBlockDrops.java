package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

public class NoBlockDrops extends GameComponent
{
    
    public NoBlockDrops(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 99999, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onBlockBreak(BlockBreakEvent event){
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
    }
}
