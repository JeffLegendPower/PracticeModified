package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic;
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
    public BreakResetComponent(GameLogic logic, String s)
    {
        super(logic);
        mat = new Gson().fromJson(s, Material.class);
    }
    @Override
    public String getData() {return new Gson().toJson(mat);}

    @Override
    public void onEvent(GameEvent e) {}

    @Override
    public void onEvent(Event event)
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
            e.setCancelled(true);
            ((SoloGameLogic)logic).reset(true);
        }
    }
}