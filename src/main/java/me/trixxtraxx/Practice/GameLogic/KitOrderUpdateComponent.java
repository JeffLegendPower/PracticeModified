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
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitOrderUpdateComponent extends GameComponent
{
    private List<ItemStorage> ItemData = new List<>();
    
    private class ItemStorage{
        public UUID uuid;
        public int itemIndex;
        public String player;
        
        public ItemStorage(UUID uuid, int ItemIndex, String p){
            this.uuid = uuid;
            this.itemIndex = ItemIndex;
            this.player = p;
        }
    }
    
    public KitOrderUpdateComponent(GameLogic logic)
    {
        super(logic);
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onItemSet(KitSetItemEvent event)
    {
        UUID uuid = UUID.randomUUID();
        event.getItem().addCustomData("kit_order_id", uuid.toString());
        Practice.log(4, "Adding Item data: " + uuid.toString() + " for " + event.getItem().getType());
        ItemData.add(new ItemStorage(uuid, event.getItemIndex(), event.getPracticePlayer().getPlayer().getName()));
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onInvReset(KitSetEvent event)
    {
        ItemData.removeAll(x -> x.player.equalsIgnoreCase(event.getPlayer().getName()));
        Practice.log(4, "Removing Item data for " + event.getPlayer().getName());
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onUpdate(InventoryCloseEvent event)
    {
        if(event.getInventory().getType() != InventoryType.CRAFTING) {
            Practice.log(4, "Closed Inv wasnt from a player; type = " + event.getInventory().getType());
            return;
        }
        
        int kitId = logic.getGame().getKit().getSqlId();
        if(kitId == -1) {
            Practice.log(4, "Game Kit was not in sql");
            return;
        }
        
        PracticePlayer pp = PracticePlayer.getPlayer((Player) event.getPlayer());
        Kit k = logic.getGame().getKit();
        Inventory inv = event.getPlayer().getInventory();
    
        List<ItemStack> items = k.getItemStacks();
        HashMap<Integer, Integer> order = k.getOrder(pp);
    
        HashMap<Integer, Integer> newOrder = new HashMap<>();
        int index = 0;
        List<ItemStorage> storages = ItemData.findAll(x -> x.player.equalsIgnoreCase(event.getPlayer().getName()));
        for(Map.Entry<Integer, Integer> orderEntry : order.entrySet())
        {
            int itemIndex = Integer.parseInt(String.valueOf(orderEntry.getKey()));
            int value =(int) Double.parseDouble(String.valueOf(orderEntry.getValue()));
            ItemStorage storage = storages.find(x -> x.itemIndex == itemIndex);
            if(storage == null) newOrder.put(itemIndex, value);
            else{
                ItemStack currentStack = new List<>(inv.getContents()).find(x -> {
                    if(x == null) return false;
                    BetterItem item = new BetterItem(x);
                    String uuid = item.getCustomData("kit_order_id");
                    Practice.log(4, "uuid = " + uuid + "; storage.uuid = " + storage.uuid);
                    return uuid.equalsIgnoreCase(storage.uuid.toString());
                });
                if(currentStack == null) newOrder.put(itemIndex, value);
                else
                {
                    int slot = inv.first(currentStack);
                    Practice.log(4, "found slot " + slot + " for " + currentStack.getType().name());
                    newOrder.put(itemIndex, slot);
                }
            }
        }
        
        boolean update = false;
        //compare order and newOrder, if they are different, update the order
        if(order.size() != newOrder.size()) update = true;
        else
        {
            for(int i = 0; i < newOrder.size() && i < order.size(); i++)
            {
                if(
                        Integer.parseInt(String.valueOf(newOrder.get(i))) != Integer.parseInt(String.valueOf(order.get(i))) ||
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
