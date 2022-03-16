package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
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
    public PressurePlateResetComponent(GameLogic logic, String s)
    {
        super(logic);
    }
    @Override
    public String getData() {return "{}";}

    @Override
    public void onEvent(GameEvent e) {}

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof BlockRedstoneEvent)
        {
            onPlateStep((BlockRedstoneEvent) event);
        }
    }

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
