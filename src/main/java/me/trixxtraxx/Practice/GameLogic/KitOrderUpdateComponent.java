package me.trixxtraxx.Practice.GameLogic;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Kit.Events.KitSetEvent;
import me.trixxtraxx.Practice.Kit.Events.KitSetItemEvent;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitOrderUpdateComponent extends GameComponent
{
    private HashMap<String, Kit> lastApplied = new HashMap<>();
    
    public KitOrderUpdateComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onItemSet(KitSetItemEvent event)
    {
        UUID uuid = UUID.randomUUID();
        BetterItem item = event.getItem();
        item.addCustomData("kit_order_index", event.getItemIndex() + "");
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onKitSet(KitSetEvent event)
    {
        lastApplied.put(event.getPlayer().getName(), event.getKit());
    }
    
    @TriggerEvent
    public void onDeath(PlayerDeathEvent event){
        cancel = true;
        new BukkitRunnable(){
    
            @Override
            public void run()
            {
                cancel = false;
            }
        }.runTaskLater(Practice.Instance, 1);
    }
    
    public boolean cancel = false;
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onUpdate(InventoryCloseEvent event)
    {
        if(logic.getGame().getStartTime() + 12000 > System.currentTimeMillis()) return;
        if(cancel) return;
        if(event.getInventory().getType() != InventoryType.CRAFTING) {
            Practice.log(4, "Closed Inv wasnt from a player; type = " + event.getInventory().getType());
            return;
        }
        
        Kit k = lastApplied.get(event.getPlayer().getName());
        if(k == null){
            Practice.log(4, "Closed Inv wasnt from a player; no kit");
            return;
        }
        int kitId = k.getSqlId();
        if(kitId == -1) {
            Practice.log(4, "Game Kit was not in sql");
            return;
        }
        
        PracticePlayer pp = PracticePlayer.getPlayer((Player) event.getPlayer());
        
        PlayerInventory inv = event.getPlayer().getInventory();
        
        List<ItemStack> items = k.getItemStacks();
        
        HashMap<Integer, Integer> order = k.getOrder(pp);
        HashMap<Integer, Integer> newOrder = new HashMap<>();
        
        List<ItemStack> invItems = new List<>(inv.getContents());
        invItems.addAll(inv.getArmorContents());
        for(ItemStack stack : invItems)
        {
            if(stack == null || (stack.getType() == Material.POTION && (stack.getDurability() == 16421 || stack.getDurability() == 16453))) continue;
            BetterItem item = new BetterItem(stack);
            if(item.getType() == Material.AIR) continue;
            int kitIndex = -1;
            try{
                kitIndex = Integer.parseInt(item.getCustomData("kit_order_index"));
            }
            catch(Exception e){
                Practice.log(2, "Failed to get kit order for " + stack);
                continue;
            }
            int slot = inv.first(stack);
            if(slot == -1) {
                if(inv.getHelmet().isSimilar(stack)) slot = 39;
                else if(inv.getChestplate().isSimilar(stack)) slot = 38;
                else if(inv.getLeggings().isSimilar(stack)) slot = 37;
                else if(inv.getBoots().isSimilar(stack)) slot = 36;
            }
            if(slot == -1) {
                Practice.log(2, "Failed to find slot for " + stack);
                continue;
            }
            newOrder.put(kitIndex, slot);
        }
        
        boolean update = false;
        //compare order and newOrder, if they are different, update the order
        if(order.size() != newOrder.size()) update = true;
        else
        {
            for(int i = 0; i < newOrder.size() && i < order.size(); i++)
            {
                String val1 = String.valueOf(newOrder.get(i));
                String val2 = String.valueOf(order.get(i));
                Practice.log(4, "val1 = " + val1 + " val2 = " + val2);
                if(
                        !val1.equalsIgnoreCase(val2) ||
                        (int)Double.parseDouble(String.valueOf(newOrder.keySet().toArray()[i])) != (int) Double.parseDouble(String.valueOf(order.keySet().toArray()[i]))
                )
                {
                    update = true;
                    break;
                }
            }
        }
        if(update)
        {
            pp.getPlayer().sendMessage(ChatColor.BLUE + "Your new Inventory Layout has been set!");
            pp.setCustomOrder(kitId, newOrder);
        }
    }
}
