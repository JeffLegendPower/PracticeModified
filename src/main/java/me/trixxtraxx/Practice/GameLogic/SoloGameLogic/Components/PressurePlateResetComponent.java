package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class PressurePlateResetComponent extends GameComponent
{
    public PressurePlateResetComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onPlateStep(BlockRedstoneEvent e)
    {
        if(e.getBlock().getType() == Material.WOOD_PLATE ||
                e.getBlock().getType() == Material.IRON_PLATE ||
                e.getBlock().getType() == Material.GOLD_PLATE ||
                e.getBlock().getType() == Material.STONE_PLATE)
        {
            if (logic instanceof SoloGameLogic)
            {
                ((SoloGameLogic) logic).reset(true);
            }
        }
    }
}
