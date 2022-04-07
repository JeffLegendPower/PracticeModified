package me.trixxtraxx.Practice.Kit;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.ComponentClass;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.ConfigItem;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kit extends ComponentClass<KitComponent>
{
    private int sqlId;
    private int defaultOrderId;
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

    public Kit(String name, int sqlId, List<ItemStack> stacks, int defaultOrderId, HashMap<Integer,Integer> defaultOrder)
    {
        items = stacks;
        this.defaultOrder = defaultOrder;
        this.sqlId = sqlId;
        this.defaultOrderId = defaultOrderId;
        this.name = name;
    }

    public int getSqlId(){return sqlId;}
    public int getDefaultOrderId(){return defaultOrderId;}
    public String getName(){return name;}
    public String getItems()
    {
        List<ConfigItem> Items = new ArrayList<>();
        for (ItemStack i:items)
        {
            ConfigItem i2 = new ConfigItem();
            i2.stack = new Gson().toJson(i.serialize());
            Items.add(i2);
        }
        return new Gson().toJson(Items);
    }
    public String getDefaultOrder(){return new Gson().toJson(defaultOrder);}
    public void setSqlId(int id){sqlId = id;}
    public void setDefaultOrderId(int id){defaultOrderId = id;}

    public void setInventory(Player p)
    {
        clearInventory(p);
        setItems(p);
    }
    
    public void setNewItems(List<ItemStack> stacks){items = stacks;}
    public void setNewDefaultOrder(HashMap<Integer,Integer> defaultOrder){this.defaultOrder = defaultOrder;}

    private void setItems(Player p)
    {
        PlayerInventory inv = p.getInventory();
        HashMap<Integer, Integer> order = defaultOrder;
        PracticePlayer prac = PracticePlayer.getPlayer(p);
        if(prac.getCustomOrder(sqlId) != null) order = prac.getCustomOrder(sqlId);

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) indexes.add(i);

        //Practice.log(4, "Default Order: " + order.size() + "," + order.entrySet() + "," + (order.size() == defaultOrder.size()) );

        for (Map.Entry<Integer, Integer> entry: order.entrySet())
        {
            try
            {
                int key = Integer.parseInt((String) String.valueOf(entry.getKey()));
                //Practice.log(4, "Key: " + key);
                //SOME SHITTY BUG WITH HASHMAP JSON SERIALIZATION "CANT CAST STRING TO INT"
                ItemStack stack = items.get(key);
                //SOME SHITTY BUG WITH HASHMAP JSON SERIALIZATION "CANT CAST DOUBLE TO INT"
                int slot = (int) Double.parseDouble(String.valueOf(entry.getValue()));
                //Practice.log(4, "Now Setting: " + slot + "," + stack );
                inv.setItem(slot, stack.clone());
                indexes.remove((Integer) key);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        for (int index:indexes)
        {
            ItemStack stack = items.get(index);
            Practice.log(4, "Seting remaining stack: " + index + "," + stack);
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