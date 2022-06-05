package me.trixxtraxx.Practice.Kit;

import com.google.gson.Gson;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.trixxtraxx.Practice.ComponentClass;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Gamemode.Game;
import me.trixxtraxx.Practice.Kit.Events.KitSetEvent;
import me.trixxtraxx.Practice.Kit.Events.KitSetItemEvent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.ConfigItem;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import me.TrixxTraxx.Linq.List;
import java.util.Map;
import java.util.UUID;

public class Kit extends ComponentClass<KitComponent>
{
    private int sqlId;
    private String name;
    private List<ItemStack> items;
    //This is rly a hashmap of String, Double lmao
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

    public Kit(String name, int sqlId, List<ItemStack> stacks, HashMap<Integer,Integer> defaultOrder)
    {
        items = stacks;
        this.defaultOrder = defaultOrder;
        this.sqlId = sqlId;
        this.name = name;
    }

    public int getSqlId(){return sqlId;}
    public String getName(){return name;}
    public String getItems()
    {
        ItemStack[] stacks = new ItemStack[items.size()];
        items.toArray(stacks);
        String serialized = BetterItem.serialize(stacks);
        return "{Item:\"" + serialized + "\"}";
    }
    public List<ItemStack> getItemStacks() {return items;}
    public String getDefaultOrder(){return new Gson().toJson(defaultOrder);}
    public void setSqlId(int id){sqlId = id;}

    public void setInventory(Player p, boolean useCustomOrder)
    {
        clearInventory(p);
        setItems(p, useCustomOrder);
    }
    
    public void setNewItems(List<ItemStack> stacks){items = stacks;}
    public void setNewDefaultOrder(HashMap<Integer,Integer> defaultOrder){this.defaultOrder = defaultOrder;}

    private void setItems(Player p, boolean useCustomOrder)
    {
        Game g = Game.getGame(p);
        PlayerInventory inv = p.getInventory();
        PracticePlayer prac = PracticePlayer.getPlayer(p);
        if(g != null) if(g.getLogic().triggerEvent(new KitSetEvent(g.getLogic(), this, prac)).isCanceled()) return;
        HashMap<Integer, Integer> order = getOrder(prac);

        List<Integer> indexes = new List<>();
        for (int i = 0; i < items.size(); i++) indexes.add(i);

        Practice.log(4, "Order: " + order.size() + "," + order.entrySet() + ",default size: " + defaultOrder.size());

        for (Map.Entry<Integer, Integer> entry: order.entrySet())
        {
            try
            {
                int key = Integer.parseInt(String.valueOf(entry.getKey()));
                //Practice.log(4, "Key: " + key);
                //SOME SHITTY BUG WITH HASHMAP JSON SERIALIZATION "CANT CAST STRING TO INT"
                if(items.size() <= key)
                {
                    Practice.log(2, "Order Contains a Key out of bounds: " + key + "," + items.size());
                    continue;
                }
                
                ItemStack stack = items.get(key);
                //SOME SHITTY BUG WITH HASHMAP JSON SERIALIZATION "CANT CAST DOUBLE TO INT"
                int slot = (int) Double.parseDouble(String.valueOf(entry.getValue()));
                //Practice.log(4, "Now Setting: " + slot + "," + stack );
                BetterItem newItem = new BetterItem(stack);
                if(g != null)
                {
                    KitSetItemEvent event = new KitSetItemEvent(g.getLogic(), prac, newItem, slot, key);
                    if(g.getLogic().triggerEvent(event).isCanceled()) continue;
                    if(inv.getItem(event.getSlot()) != null){
                        if(inv.firstEmpty() == -1){
                            if(inv.getHelmet() == null) inv.setHelmet(event.getItem());
                            else if(inv.getChestplate() == null) inv.setChestplate(event.getItem());
                            else if(inv.getLeggings() == null) inv.setLeggings(event.getItem());
                            else if(inv.getBoots() == null) inv.setBoots(event.getItem());
                            else{
                                Practice.log(1, "Inventory is full");
                            }
                        }
                        else{
                            inv.addItem(event.getItem());
                        }
                    }
                    else{
                        inv.setItem(event.getSlot(), event.getItem());
                    }
                }
                else
                {
                    if(inv.getItem(slot) != null){
                        inv.addItem(newItem);
                    }
                    else{
                        inv.setItem(slot, newItem);
                    }
                }
                
                indexes.remove((Integer) key);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        for (int index:indexes)
        {
            BetterItem stack = new BetterItem(items.get(index));
            if(g != null)
            {
                KitSetItemEvent event = new KitSetItemEvent(g.getLogic(), prac, stack, -1, index);
                if(g.getLogic().triggerEvent(event).isCanceled()) continue;
            }
            Practice.log(4, "Setting remaining stack: " + index + "," + stack);
            if(inv.firstEmpty() == -1){
                if(inv.getHelmet() == null) inv.setHelmet(stack.clone());
                else if(inv.getChestplate() == null) inv.setChestplate(stack.clone());
                else if(inv.getLeggings() == null) inv.setLeggings(stack.clone());
                else if(inv.getBoots() == null) inv.setBoots(stack.clone());
                else{
                    Practice.log(1, "Inventory is full");
                }
            }
            else
            {
                inv.addItem(stack.clone());
            }
        }
    }
    
    public HashMap<Integer, Integer> getOrder(PracticePlayer prac)
    {
        HashMap<Integer, Integer> order = defaultOrder;
        Object customOrder = prac.getCustomOrder(sqlId);
        if(customOrder != null) order = (HashMap<Integer, Integer>) customOrder;
        return order;
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