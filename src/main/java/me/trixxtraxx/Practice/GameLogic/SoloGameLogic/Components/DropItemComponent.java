package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.CustomValue;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.ResetEvent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import me.TrixxTraxx.Linq.List;

public class DropItemComponent extends GameComponent implements CustomValue
{
    @Config
    private Material drop;
    @Config
    private List<Material> removeItems;
    @Config
    private boolean cancelEvent = true;
    
    private boolean hasDroped = false;

    public DropItemComponent(GameLogic logic, Material drop, List<Material> removeItems, boolean cancelEvent)
    {
        super(logic);
        this.drop = drop;
        this.removeItems = removeItems;
        this.cancelEvent = cancelEvent;
        Practice.log(4, "DropItemComponent with " + removeItems.size() + " items");
    }
    public DropItemComponent(GameLogic logic){super(logic);}
    
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    @SuppressWarnings("deprecation")
    public void onDrop(PlayerInteractEvent e)
    {
        if(hasDroped) return;
        if(e.getItem() != null && e.getItem().getType() == drop)
        {
            if(logic.triggerEvent(new DropEvent(logic)).isCanceled()) return;
            if(cancelEvent) e.setCancelled(true);

            PlayerInventory inv = e.getPlayer().getInventory();
            for (Material mat:removeItems) inv.remove(mat);
            hasDroped = true;
        }
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
    public void onDropEvent(ResetEvent e){
        hasDroped = false;
    }
    
    @Override
    public String getValue()
    {
        //serialize it with key=value<>key=value
        StringBuilder sb = new StringBuilder();
        sb.append("drop=" + drop.toString());
        sb.append("<>");
        sb.append("cancelEvent=" + cancelEvent);
        sb.append("<>");
        for(Material s:removeItems)
        {
            sb.append(s.toString());
            sb.append(";");
        }
        return sb.toString();
    }
    
    @Override
    public void applyValue(String value)
    {
        //deserialize it with key=value<>key=value
        String[] split = value.split("<>");
        drop = Material.valueOf(split[0].split("=")[1]);
        cancelEvent = Boolean.parseBoolean(split[1].split("=")[1]);
        removeItems = new List<Material>();
        String[] split2 = split[2].split(";");
        for(String s:split2)
        {
            removeItems.add(Material.valueOf(s));
        }
    }
}