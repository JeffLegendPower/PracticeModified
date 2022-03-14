package me.trixxtraxx.Practice.Kit;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kit
{
    private int sqlId;
    private int defaultOrderId;
    private String name;
    private List<KitComponent> components = new ArrayList<>();
    private List<ItemStack> items;
    private HashMap<Integer, Integer> defaultOrder;

    //- different Items (duh)
    //- double jump
    //- health modification
    //- custom tnt timer
    //- tnt instand prime
    //- map break
    //- no natural regeneration
    //- permanent effects
    //- soup heal
    //- nohunger
    //- no fall damage
    //- no Item Drops
    //- No Damage

    public Kit(String name, int sqlId, List<ItemStack> stacks, int defaultOrderId, HashMap<Integer,Integer> defaultOrder)
    {
        items = stacks;
        this.defaultOrder = defaultOrder;
        this.sqlId = sqlId;
        this.defaultOrderId = defaultOrderId;
        this.name = name;
    }

    public List<KitComponent> getComponents()
    {
        return components;
    }
    public void addComponent(KitComponent comp)
    {
        components.add(comp);
    }
    public void removeComponent(KitComponent comp)
    {
        components.remove(comp);
    }
    public List<KitComponent> getComponents(Class<?> c)
    {
        List<KitComponent> comps = new ArrayList<>();
        for (KitComponent comp:components)
        {
            if(c.isInstance(comp)) comps.add(comp);
        }
        return comps;
    }

    public int getSqlId(){return sqlId;}
    public int getDefaultOrderId(){return defaultOrderId;}
    public String getName(){return name;}
    public String getItems(){return new Gson().toJson(items);}
    public String getDefaultOrder(){return new Gson().toJson(defaultOrder);}
    public void setSqlId(int id){sqlId = id;}
    public void setDefaultOrderId(int id){defaultOrderId = id;}

    public void setInventory(Player p)
    {
        clearInventory(p);
        setItems(p);
    }

    private void setItems(Player p)
    {
        PlayerInventory inv = p.getInventory();
        HashMap<Integer, Integer> order = defaultOrder;
        PracticePlayer prac = PracticePlayer.getPlayer(p);
        //add custom player order, unable to in the current state
        //if(prac.) order = playerOrders.get(p);

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) indexes.add(i);

        for (Map.Entry<Integer, Integer> entry: order.entrySet())
        {
            try
            {
                ItemStack stack = items.get(entry.getKey());
                inv.setItem(entry.getValue(), stack.clone());
                indexes.remove(entry.getKey());
            }
            catch (Exception ex){}
        }

        for (int index:indexes)
        {
            ItemStack stack = items.get(index);
            inv.addItem(stack.clone());
        }
    }

    private void clearInventory(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);
        if(p.getOpenInventory() != null && p.getOpenInventory().getType() == InventoryType.PLAYER) p.closeInventory();
    }
}