package me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.bwp.GameLogic.Components.ComponentEvent;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.SoloGameLogic;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakResetComponent extends GameComponent
{
    private Material mat;

    public BreakResetComponent(GameLogic logic, Material material)
    {
        super(logic);
        mat = material;
    }

    @Override
    public void onEventList(ComponentEvent e)
    {

    }

    @Override
    public void onEventList(Event event)
    {
        if(event instanceof BlockBreakEvent)
        {
            onBlockBreak((BlockBreakEvent) event);
        }
    }

    public void onBlockBreak(BlockBreakEvent e)
    {
        if(e.getBlock().getType() != mat) return;
        if(logic instanceof SoloGameLogic)
        {
            ((SoloGameLogic)logic).reset();
        }
    }
}