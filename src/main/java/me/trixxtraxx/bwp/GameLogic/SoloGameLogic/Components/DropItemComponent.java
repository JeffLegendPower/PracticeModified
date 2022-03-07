package me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Components;

import me.trixxtraxx.bwp.BWP;
import me.trixxtraxx.bwp.GameEvents.GameEvent;
import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.GameLogic.GameLogic;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.bwp.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.bwp.Utils.Region;
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
    private Region remove;
    private List<ItemStorage> stored = new ArrayList<>();

    private class ItemStorage
    {
        protected Material mat;
        protected byte data;
    }

    public DropItemComponent(GameLogic logic, Material drop, List<Material> removeItems, Region remove)
    {
        super(logic);
        this.drop = drop;
        this.removeItems = removeItems;
        this.remove = remove;
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerInteractEvent) onDrop((PlayerInteractEvent) event);
    }

    @Override
    public void onEvent(GameEvent event)
    {
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
    }

    @SuppressWarnings("deprecation")
    public void onDrop(PlayerInteractEvent e)
    {
        if(e.getItem() != null && e.getItem().getType() == drop)
        {
            if(logic.triggerEvent(new DropEvent(logic)).isCanceled()) return;
            e.setCancelled(true);
            for (Location loc:remove.getLocations())
            {
                ItemStorage store = new ItemStorage();
                Block b = loc.getBlock();
                store.mat = b.getType();
                store.data = b.getData();
                stored.add(store);
                b.setType(Material.AIR);
            }

            PlayerInventory inv = e.getPlayer().getInventory();
            for (Material mat:removeItems) inv.remove(mat);
        }
    }

    @SuppressWarnings("deprecation")
    public void onReset(ResetEvent e)
    {
        if(stored.size() == 0) return;
        int i = 0;
        for (Location loc:remove.getLocations())
        {
            ItemStorage store = stored.get(i);
            loc.getBlock().setType(store.mat);
            loc.getBlock().setData(store.data);
            i++;
        }
        stored.clear();
    }
}