package me.trixxtraxx.Practice.GameLogic.Components.Components.InventoryView;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryView
{
    public String ItemStacks;
    public List<Integer> Slots;
    public String uuid;
    
    public void open(Player p){
        BetterInv inv = new BetterInv(p, 45, "§9Inventory View");
        ItemStack[] Items = BetterItem.deserialize(ItemStacks);
        
        for(int i = 0; i < Slots.size() && i < Items.length; i++){
            inv.setItem(Slots.get(i), Items[i]);
            inv.lock(Slots.get(i));
        }
    }
}
