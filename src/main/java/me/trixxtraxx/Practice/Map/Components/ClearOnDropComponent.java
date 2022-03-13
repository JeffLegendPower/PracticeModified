package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class ClearOnDropComponent extends MapComponent
{
    private Region remove;
    private List<ItemStorage> stored = new ArrayList<>();

    private class ItemStorage
    {
        protected Material mat;
        protected byte data;
    }

    public ClearOnDropComponent(Map m, Region remove)
    {
        super(m);
        this.remove = remove;
    }

    public ClearOnDropComponent(Map map, String data)
    {
        super(map);
        remove = new Gson().fromJson(data, Region.class);
    }

    @Override
    public String getData()
    {
        return new Gson().toJson(remove);
    }

    @Override
    public void onEvent(GameEvent event)
    {
        if(event instanceof ResetEvent) onReset((ResetEvent) event);
        if(event instanceof DropEvent) onDrop((DropEvent) event);
    }

    @SuppressWarnings("deprecation")
    public void onDrop(DropEvent e)
    {
        for (Location loc:remove.getLocations())
        {
            ItemStorage store = new ItemStorage();
            Block b = loc.getBlock();
            store.mat = b.getType();
            store.data = b.getData();
            stored.add(store);
            b.setType(Material.AIR);
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
