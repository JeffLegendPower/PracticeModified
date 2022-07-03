package me.trixxtraxx.Practice.Map.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import me.trixxtraxx.Practice.Utils.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearOnDropComponent extends MapComponent
{
    @Config
    private Region remove;
    private List<ItemStorage> stored = new List<>();

    private class ItemStorage
    {
        protected Material mat;
        protected byte data;
    }

    public ClearOnDropComponent(Map m, Region remove)
    {
        super(m);
        Practice.log(4, "Drop Component Manually added");
        this.remove = remove;
    }

    public ClearOnDropComponent(Map map)
    {
        super(map);
        Practice.log(4, "Drop Component Automatically added");
    }
    
    @TriggerEvent(priority = 2, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    @SuppressWarnings("deprecation")
    public void onEvent1(DropEvent e)
    {
        boolean needStore = true;
        if(stored.size() != 0) needStore = false;
        Practice.log(4, "Clearing Drop Area");
        for (Location loc:remove.getLocations(e.getlogic().getWorld()))
        {
            Block b = loc.getBlock();
            if(needStore)
            {
                ItemStorage store = new ItemStorage();
                store.mat = b.getType();
                store.data = b.getData();
                stored.add(store);
            }
            b.setType(Material.AIR);
        }
    }
    
    @TriggerEvent(priority = 999, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    @SuppressWarnings("deprecation")
    public void onEvent2(ResetEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(stored.size() == 0) return;
                Practice.log(4, "Reseting Drop Area");
                int i = 0;
                for(Location loc: remove.getLocations(e.getlogic().getWorld()))
                {
                    ItemStorage store = stored.get(i);
                    loc.getBlock().setType(store.mat);
                    loc.getBlock().setData(store.data);
                    i++;
                }
            }
        }.runTaskLater(Practice.Instance, 0);
    }
}
