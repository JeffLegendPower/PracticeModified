package me.trixxtraxx.Practice.Kit.Editor.Potion;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Kit.Editor.EditorInventory;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class PotionEditor extends EditorInventory
{
    private static List<Integer> placeholders = new List<>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 21, 22, 23, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);;
    private static List<Integer> potionSlots = new List<>(19,20,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43);
    
    public PotionEditor(Player p)
    {
        super(p, "Potion Editor", 54);
    }
    
    @Override
    public void fill()
    {
        BetterItem placeholder = new BetterItem(Material.STAINED_GLASS_PANE, 1, (byte) 7).setDisplayName(" ");
        for(int slot : placeholders)
        {
            inv.setItem(slot, placeholder);
            inv.lock(slot);
        }
        
        inv.setItem(13, new BetterItem(Material.POTION, 1, (byte) 0).setDisplayName("Custom Potion"));
        
        showPotions();
    }
    
    public void showPotions()
    {
        clearPotionSpace();
        int index = 0;
        for(Effect ef : Effect.values())
        {
            int slot = potionSlots.get(index);
            index++;
            inv.setItem(slot, ef.getItem());
            inv.onClickSlot(slot, (c) -> {
                c.setCancelled(true);
                setPotion(ef, 1200, 0);
            });
        }
    }
    
    public void setPotion(Effect ef, int duration, int amplifier)
    {
        clearPotionSpace();
        ItemStack stack = inv.getItem(13);
        if(stack == null || stack.getType() != Material.POTION) {
            showPotions();
            p.sendMessage("§cYou must select a potion first!");
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
            return;
        }
        inv.setItem(29, new BetterItem(Material.POTION, 1, (byte) ef.getId()).setDisplayName("§9Set Potion Color"));
        inv.setItem(31, new BetterItem(Material.BEACON, amplifier + 1).setDisplayName("§9Amplifier: §b" + (amplifier + 1)).setLore("§9Right Click to Increase", "§9Left Click to Decrease"));
        inv.setItem(33, new BetterItem(Material.INK_SACK, 1, (byte) 10).setDisplayName("§9Apply"));
        inv.setItem(34, new BetterItem(Material.INK_SACK, 1, (byte) 1).setDisplayName("§cRemove Potion Effect"));
        inv.setItem(28, new BetterItem(Material.BARRIER, 1).setDisplayName("§9Cancel"));
        
        inv.onClickSlot(28, (c) -> {
            c.setCancelled(true);
            showPotions();
        });
        inv.onClickSlot(29, (c) ->
        {
            c.setCancelled(true);
            stack.setDurability((byte) ef.getId());
            PotionMeta meta = (PotionMeta) stack.getItemMeta();
            meta.setMainEffect(null);
            meta.removeCustomEffect(ef.getType());
            stack.setItemMeta(meta);
            showPotions();
        });
        inv.onClick((c) ->{
            if(c.getSlot() == 31 && c.getEvent() instanceof InventoryClickEvent)
            {
                c.setCancelled(true);
                InventoryClickEvent e = (InventoryClickEvent) c.getEvent();
                if(e.getClick() == ClickType.RIGHT)
                {
                    if(amplifier == 0) return;
                    setPotion(ef, duration, amplifier - 1);
                }
                else if(e.getClick() == ClickType.LEFT)
                {
                    setPotion(ef, duration, amplifier + 1);
                }
            }
        });
        inv.onClickSlot(33, (c) ->
        {
            c.setCancelled(true);
            Practice.log(4, "adding effect");
            addEffect(stack, ef, duration, amplifier);
            Practice.log(4, "Effect added, now refreshing");
            showPotions();
        });
        inv.onClickSlot(34, (c) ->
        {
            c.setCancelled(true);
            Practice.log(4, "removing effect");
            removeEffect(stack, ef);
            Practice.log(4, "Effect Removed, now refreshing");
            showPotions();
        });
        
        inv.setItem(37, new BetterItem(Material.STAINED_GLASS, 1, (byte) 14).setDisplayName("§9Remove 1 Minute from Duration"));
        inv.setItem(38, new BetterItem(Material.STAINED_GLASS_PANE, 10, (byte) 14).setDisplayName("§9Remove 10 Seconds from Duration"));
        inv.setItem(39, new BetterItem(Material.STAINED_GLASS, 1, (byte) 14).setDisplayName("§9Remove 1 Second from Duration"));
        inv.setItem(40, new BetterItem(Material.WATCH).setDisplayName("§9Duration: §b" + (duration / 20) + "s"));
        inv.setItem(41, new BetterItem(Material.STAINED_GLASS, 1, (byte) 5).setDisplayName("§9Add 1 Second to Duration"));
        inv.setItem(42, new BetterItem(Material.STAINED_GLASS_PANE, 10, (byte) 5).setDisplayName("§9Add 10 Seconds to Duration"));
        inv.setItem(43, new BetterItem(Material.STAINED_GLASS, 1, (byte) 5).setDisplayName("§9Add 1 Minute to Duration"));
        
        inv.onClickSlot(37, (c) -> {
            c.setCancelled(true);
            setPotion(ef, duration - 1200 < 1 ? 1 : duration - 1200, amplifier);
        });
        inv.onClickSlot(38, (c) -> {
            c.setCancelled(true);
            setPotion(ef, duration - 200 < 1 ? 1 : duration - 200, amplifier);
        });
        inv.onClickSlot(39, (c) -> {
            c.setCancelled(true);
            setPotion(ef, duration - 20 < 1 ? 1 : duration - 20, amplifier);
        });
        inv.onClickSlot(40, (c) -> {
            c.setCancelled(true);
        });
        inv.onClickSlot(41, (c) -> {
            c.setCancelled(true);
            setPotion(ef, duration + 20, amplifier);
        });
        inv.onClickSlot(42, (c) -> {
            c.setCancelled(true);
            setPotion(ef, duration + 200, amplifier);
        });
        inv.onClickSlot(43, (c) -> {
            c.setCancelled(true);
            setPotion(ef, duration + 1200, amplifier);
        });
    }
    
    public void clearPotionSpace(){
        for(int slot : potionSlots)
        {
            inv.setItem(slot, null);
            inv.clearOnClick(slot);
        }
        inv.clearOnClick();
    }
    
    public void addEffect(ItemStack stack, Effect ef, int duration, int ampl)
    {
        removeEffect(stack, ef);
        Practice.log(4, "Now Adding Effect");
        if(stack == null || stack.getType() != Material.POTION){
            Practice.log(4, "not adding effect");
            return;
        }
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.addCustomEffect(new PotionEffect(ef.getType(), duration, ampl), true);
        Practice.log(4, "Effect Added, now doing lore");
        java.util.List<String> lore = meta.getLore();
        if(lore == null) lore = new List<>();
        Practice.log(4, "Lore Extracted");
        lore.add(ef.getDescription().replace("{Duration}", duration / 20 + "s").replace("{Amplifier}", (ampl + 1) + " "));
        meta.setLore(lore);
        Practice.log(4, "Lore Added, now setting item");
        stack.setItemMeta(meta);
        inv.setItem(13, stack);
        Practice.log(4, "Effect added!");
    }
    
    public void removeEffect(ItemStack stack, Effect ef)
    {
        Practice.log(4, "Now Removing Effect!");
        if(stack == null || stack.getType() != Material.POTION){
            Practice.log(4, "not removing effect");
            return;
        }
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.removeCustomEffect(ef.getType());
        Practice.log(4, "Effect Removed, now doing lore");
        java.util.List<String> lore = meta.getLore();
        if(lore != null)
        {
            List<String> newLore = new List();
            for(String s: lore)
            {
                if(!s.contains(ef.getName())) newLore.add(s);
            }
            meta.setLore(newLore);
        }
        Practice.log(4, "Lore Removed, now setting item");
        stack.setItemMeta(meta);
        inv.setItem(13, stack);
        Practice.log(4, "Effect removed!");
    }
}
