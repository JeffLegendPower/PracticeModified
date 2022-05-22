package me.trixxtraxx.Practice.Kit.Editor.Enchants;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Kit.Editor.EditorInventory;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentEditor extends EditorInventory
{
    private static List<Integer> placeholders = new List<>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);;
    private static List<Integer> enchantSlots = new List<>(31, 30, 32, 29, 33, 28, 34, 40, 39, 41, 38, 42, 37, 43);
    private static List<Integer> levelSlots = new List<>(28,29,30,31,32,33,34);
    
    public EnchantmentEditor(Player p)
    {
        super(p, "Enchantment Editor", 54);
        inv.onClose(x -> {
            ItemStack toEnchant = inv.getItem(13);
            if(toEnchant == null) return;
            p.getInventory().addItem(toEnchant);
        });
    }
    
    @Override
    public void fill()
    {
        BetterItem i = new BetterItem(Material.STAINED_GLASS_PANE, 1, (short) 7);
        
        for (int j : placeholders)
        {
            inv.setItem(j, i);
            inv.lock(j);
        }
        
        inv.onClickSlot(13, x -> {
            Practice.log(4, "updating enchantments");
            //delay 1 tick
            inv.getPlayer().getServer().getScheduler().runTaskLater(Practice.Instance, () -> {updateEnchantments();}, 1);
        });
    }
    
    private void updateEnchantments(){
        ItemStack toEnchant = inv.getItem(13);
        //set all slots to null that are not mentioned in the placeholders above
        clearEnchArea();
    
        if(toEnchant == null) return;
        
        HashMap<Enchantment, ItemStack> enchants = new HashMap<>();
        for(EnchantmentCategory ec : EnchantmentCategory.getCategories(toEnchant))
        {
            for(Map.Entry<Enchantment, ItemStack> e : ec.getEnchants().entrySet())
            {
                enchants.put(e.getKey(), e.getValue());
            }
        }
        int index = -1;
        for(Map.Entry<Enchantment, ItemStack> e : enchants.entrySet())
        {
            index++;
            ItemStack stack = e.getValue().clone();
            
            inv.setItem(enchantSlots.get(index), stack);
            inv.onClickSlot(enchantSlots.get(index), (x) -> {
                x.setCancelled(true);
                if(e.getKey().getMaxLevel() == 1)
                {
                    enchant(toEnchant, e.getKey(), 1);
                    updateEnchantments();
                }
                else
                {
                    setEnchantmentLevel(toEnchant, e.getKey(), e.getKey().getMaxLevel());
                }
            });
        }
    }
    
    public void setEnchantmentLevel(ItemStack toEnchant, Enchantment e, int customLevel)
    {
        //set all slots to null that are not mentioned in the placeholders above
        clearEnchArea();
        
        inv.setItem(40, new BetterItem(Material.EXP_BOTTLE).setDisplayName("§9Level: §b" + customLevel).setLore(new List<>("§9 Left Click to decrease", "§9Right Click to increase", "§9Shift Click to select")));
        inv.setItem(41, new BetterItem(Material.BARRIER).setDisplayName("§cRemove Enchantment"));
        inv.onClickSlot(41, (x) -> {
            x.setCancelled(true);
            toEnchant.removeEnchantment(e);
            updateEnchantments();
        });
        inv.onClick(x -> {
            if(x.getSlot() != 40) return;
            x.setCancelled(true);
            if(x.getEvent() instanceof InventoryClickEvent){
                InventoryClickEvent event = (InventoryClickEvent) x.getEvent();
                if(event.getClick() == ClickType.LEFT)
                {
                    setEnchantmentLevel(toEnchant, e, customLevel - 1 == 0 ? 1 : customLevel - 1);
                }
                else if(event.getClick() == ClickType.RIGHT)
                {
                    setEnchantmentLevel(toEnchant, e, customLevel + 1);
                }
                else if(event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT)
                {
                    enchant(toEnchant, e, customLevel);
                    updateEnchantments();
                }
            }
        });
        
        //set the slots 28-34 to the levels, e.getMaxLevel() is the max level, center it so when you only have 1 level it shows at slot 31, and when you have 2 levels it shows at slot 32-33, 3 at 32-34, etc.
        int level = e.getMaxLevel();
        Practice.log(4, "max level: " + level);
        int start = 3 - (level / 2);
        int lvl = 1;
        for(int i = start; lvl <= level; i++)
        {
            int finalLvl = lvl;
            inv.setItem(levelSlots.get(i), new BetterItem(Material.BOOK, lvl).setDisplayName("§9Level: §b" + finalLvl));
            inv.onClickSlot(levelSlots.get(i), (x) ->
            {
                x.setCancelled(true);
                enchant(toEnchant, e, finalLvl);
                updateEnchantments();
            });
            lvl++;
        }
    }
    
    public void clearEnchArea(){
        for(int i = 0; i < 54; i++){
            if(!placeholders.contains(i) && i != 13)
            {
                inv.setItem(i, null);
                inv.clearOnClick(i);
            }
        }
        inv.clearOnClick();
    }
    
    public void enchant(ItemStack stack, Enchantment ench, int level)
    {
        Practice.log(4, "Enchanting " + stack.getType().name() + " with " + ench.getName() + " at level " + level);
        stack.removeEnchantment(ench);
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(ench, level, true);
        stack.setItemMeta(meta);
        p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
    }
}
