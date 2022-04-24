package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.CustomValue;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
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

    public DropItemComponent(GameLogic logic, Material drop, List<Material> removeItems)
    {
        super(logic);
        this.drop = drop;
        this.removeItems = removeItems;
        Practice.log(4, "DropItemComponent with " + removeItems.size() + " items");
    }
    public DropItemComponent(GameLogic logic){super(logic);}
    
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.NONE)
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
    
    @Override
    public String getValue()
    {
        //serialize it with key=value<>key=value
        StringBuilder sb = new StringBuilder();
        sb.append(drop.toString());
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
        removeItems = new List<Material>();
        String[] split2 = split[1].split(";");
        for(String s:split2)
        {
            removeItems.add(Material.valueOf(s));
        }
    }
}