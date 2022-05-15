package me.trixxtraxx.Practice.Kit.Editor.Item;

import me.TrixxTraxx.InventoryAPI.Inventory.BetterInv;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Kit.Editor.EditorInventory;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.print.attribute.PrintRequestAttribute;
import java.util.Arrays;

public class ItemEditor extends EditorInventory
{
    private static List<Integer> placeholders = new List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 32, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44);
    private boolean filled = true;
    public ItemEditor(Player p)
    {
        super(p, "Item Editor", 45);
        inv.onClose(c ->
        {
            ItemStack stack = inv.getItem(10);
            if(stack != null){
                p.getInventory().addItem(stack);
            }
        });
    }
    
    @Override
    public void fill()
    {
        if(!filled)
        {
            Practice.log(4, "Placing Placeholders");
            BetterItem placeholder = new BetterItem(Material.STAINED_GLASS_PANE, 1, (short) 7);
            for(int place: placeholders)
            {
                inv.setItem(place, placeholder);
                inv.lock(place);
            }
        }
        setItems(true);
    }
    
    public void setItems(boolean metaDelay)
    {
        for(int i = 0; i < inv.getSize(); i++)
        {
            if(!placeholders.contains(i))
            {
                if(i != 10) inv.setItem(i, null);
                inv.clearOnClick(i);
            }
        }
        inv.clearOnClick();
        inv.clearOnItemPlace();
        inv.clearOnItemPickup();
    
        Practice.log(4, "Refreshing Inventory");
    
        if(metaDelay)
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    setItemMeta();
                }
            }.runTaskLater(Practice.Instance, 1);
        }
        else{
            setItemMeta();
        }
    
        inv.onClickSlot(13, x ->
        {
            ItemStack item = inv.getItem(10);
            x.setCancelled(true);
            if(item != null){
                ItemMeta meta = item.getItemMeta();
                if(meta == null) return;
                meta.spigot().setUnbreakable(!meta.spigot().isUnbreakable());
                item.setItemMeta(meta);
                inv.setItem(10, item);
            }
            setItems(false);
        });
    
        inv.onClickSlot(14, x ->
        {
            ItemStack item = inv.getItem(10);
            x.setCancelled(true);
            if(item != null){
                ItemMeta meta = item.getItemMeta();
                if(meta == null) return;
                if(meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)) meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                else meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                item.setItemMeta(meta);
                inv.setItem(10, item);
            }
            setItems(false);
        });
    
        inv.onClickSlot(15, x ->
        {
            ItemStack item = inv.getItem(10);
            x.setCancelled(true);
            if(item != null){
                ItemMeta meta = item.getItemMeta();
                if(meta == null) return;
                if(meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                else meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
                inv.setItem(10, item);
            }
            setItems(false);
        });
    
        inv.onClickSlot(16, x ->
        {
            ItemStack item = inv.getItem(10);
            x.setCancelled(true);
            if(item != null){
                ItemMeta meta = item.getItemMeta();
                if(meta == null) return;
                if(meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                else meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
                inv.setItem(10, item);
            }
            setItems(false);
        });
    
        inv.setItem(31, new BetterItem(Material.PAPER).setDisplayName("§9Set the Name"));
        inv.setItem(33, new BetterItem(Material.BOOK_AND_QUILL).setDisplayName("§9Set the Lore"));
    
        inv.onItemPlace(x ->
                        {
                            if(x.getSlot() != 31) return;
                            ItemStack item = inv.getItem(10);
                            if(item != null)
                            {
                                if(x.getEvent() instanceof InventoryClickEvent)
                                {
                                    InventoryClickEvent e = (InventoryClickEvent) x.getEvent();
                                    if(e.getCursor() == null || e.getCursor().getType() != Material.BOOK_AND_QUILL) {
                                        p.sendMessage("§cPlease use a book and Quill with the Name written in it!");
                                        return;
                                    }
                                    ItemMeta meta = item.getItemMeta();
                                    BookMeta book = (BookMeta) e.getCursor().getItemMeta();
                                    String name = ((BookMeta) e.getCursor().getItemMeta()).getPage(1);
                                    Practice.log(4, "Extracted Name: " + name);
                                    meta.setDisplayName(
                                            ChatColor.translateAlternateColorCodes('&', name)
                                    );
                                    item.setItemMeta(meta);
                                    inv.setItem(10, item);
                                    p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
                                }
                            }
                        });
        inv.onItemPickup(x ->
                         {
                             if(x.getSlot() != 33) return;
                             x.setCancelled(true);
                         });
    
        inv.onItemPlace(x ->
                        {
                            if(x.getSlot() != 33) return;
                            ItemStack item = inv.getItem(10);
                            if(item != null)
                            {
                                if(x.getEvent() instanceof InventoryClickEvent)
                                {
                                    InventoryClickEvent e = (InventoryClickEvent) x.getEvent();
                                    if(e.getCursor() == null || e.getCursor().getType() != Material.BOOK_AND_QUILL) {
                                        p.sendMessage("§cPlease use a book and Quill with the lore written inside of it to apply a lore!");
                                        return;
                                    }
                                    ItemMeta meta = item.getItemMeta();
                                    List<String> lore = new List<>();
                                    for(String s : ((BookMeta) e.getCursor().getItemMeta()).getPage(1).split("\n"))
                                    {
                                        Practice.log(4, "Extracted Lore: " + s);
                                        lore.add(ChatColor.translateAlternateColorCodes('&', s));
                                    }
                                    meta.setLore(lore);
                                    item.setItemMeta(meta);
                                    inv.setItem(10, item);
                                    p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
                                }
                            }
                        });
        inv.onItemPickup(x ->
                         {
                             if(x.getSlot() != 33) return;
                             x.setCancelled(true);
                         });
    }
    
    public void setItemMeta(){
        ItemStack stack = inv.getItem(10);
        inv.onClickSlot(10, x -> fill());
        BetterItem unbreakable = new BetterItem(Material.INK_SACK, 1, (short) 8).setDisplayName(
                "§9Unbreakable: §cfalse");
        BetterItem hideUnbrekabke = new BetterItem(Material.INK_SACK, 1, (short) 8).setDisplayName(
                "§9Hide Unbreakable: §cfalse");
        BetterItem hideEnchants = new BetterItem(Material.INK_SACK, 1, (short) 8).setDisplayName(
                "§9Hide Enchants: §cfalse");
        BetterItem hideAttributes = new BetterItem(Material.INK_SACK, 1, (short) 8).setDisplayName(
                "§9Hide Attributes: §cfalse");
    
        if(stack != null)
        {
            ItemMeta meta = stack.getItemMeta();
            if(meta != null)
            {
                Practice.log(4, "Refreshing Metadata Items");
                if(meta.spigot().isUnbreakable()) unbreakable.setDisplayName("§9Unbreakable: §atrue").setDurability((short) 10);
                if(meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)) hideUnbrekabke.setDisplayName("§9Hide Unbreakable: §atrue").setDurability((short) 10);
                if(meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) hideEnchants.setDisplayName("§9Hide Enchants: §atrue").setDurability((short) 10);
                if(meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) hideAttributes.setDisplayName("§9Hide Attributes: §atrue").setDurability((short) 10);
            }
        }
    
        inv.setItem(13, unbreakable);
        inv.setItem(14, hideUnbrekabke);
        inv.setItem(15, hideEnchants);
        inv.setItem(16, hideAttributes);
    }
}
