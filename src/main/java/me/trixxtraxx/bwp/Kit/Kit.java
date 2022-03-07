package me.trixxtraxx.bwp.Kit;

import me.trixxtraxx.bwp.Map.MapComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kit
{
    protected List<KitComponent> components = new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();
    private HashMap<Integer, Integer> defaultOrder = new HashMap<>();
    private HashMap<Player, HashMap<Integer, Integer>> playerOrders = new HashMap<>();

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


    public void setInventory(Player p)
    {
        clearInventory(p);
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