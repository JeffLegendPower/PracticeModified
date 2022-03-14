package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class DropItemComponent extends GameComponent
{
    private Material drop;
    private List<Material> removeItems;

    public DropItemComponent(GameLogic logic, Material drop, List<Material> removeItems)
    {
        super(logic);
        this.drop = drop;
        this.removeItems = removeItems;
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerInteractEvent) onDrop((PlayerInteractEvent) event);
    }

    @SuppressWarnings("deprecation")
    public void onDrop(PlayerInteractEvent e)
    {
        if(e.getItem() != null && e.getItem().getType() == drop)
        {
            if(logic.triggerEvent(new DropEvent(logic)).isCanceled()) return;
            e.setCancelled(true);

            PlayerInventory inv = e.getPlayer().getInventory();
            for (Material mat:removeItems) inv.remove(mat);
        }
    }
}