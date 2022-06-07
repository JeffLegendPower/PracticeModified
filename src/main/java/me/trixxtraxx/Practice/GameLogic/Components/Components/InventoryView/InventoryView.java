package me.trixxtraxx.Practice.GameLogic.Components.Components.InventoryView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class InventoryView
{
    public String player;
    public List<ItemStack> items;
    public List<Integer> slots;
    public int id;
    
    public void open(Player p){
        BetterInv inv = new BetterInv(p, 45, "§9Inventory View");
        
        Practice.log(4, "InventoryView: " + items.size() + " - " + slots.size());
        
        for(int i = 0; i < slots.size() && i < items.size(); i++){
            int slot = (int) Double.parseDouble(String.valueOf(slots.get(i)));
            inv.setItem(slot, items.get(i));
            inv.lock(slot);
        }
        inv.open();
    }
    
    public String getItems()
    {
        List<String> stacks = new List<>();
        for(ItemStack item : items)
        {
            stacks.add(JsonItemStack.toJson(item));
        }
        return new Gson().toJson(stacks);
    }
}
