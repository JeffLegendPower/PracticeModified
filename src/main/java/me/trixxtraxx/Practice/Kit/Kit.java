package me.trixxtraxx.Practice.Kit;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
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
    protected List<KitComponent> components = new ArrayList<>();
    private List<ItemStack> items;
    private HashMap<Integer, Integer> defaultOrder;
    private HashMap<Player, HashMap<Integer, Integer>> playerOrders = new HashMap<>();

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

    public Kit(List<ItemStack> stacks, HashMap<Integer,Integer> defaultOrder)
    {
        items = stacks;
        this.defaultOrder = defaultOrder;
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

    public void setInventory(Player p)
    {
        clearInventory(p);
        setItems(p);
    }

    private void setItems(Player p)
    {
        PlayerInventory inv = p.getInventory();
        HashMap<Integer, Integer> order = defaultOrder;
        if(playerOrders.containsKey(p)) order = playerOrders.get(p);

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