package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.trixxtraxx.Practice.GameLogic.Components.Config;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.TriggerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import me.TrixxTraxx.Linq.List;

public class SettingsComponent extends GameComponent implements ISettingsComponent
{
    @Config
    private Material mat;
    @Config
    private String title = ChatColor.AQUA + "Settings";
    @Config
    private String difficultyTitle = ChatColor.AQUA + "Difficulty";
    @Config
    private String toolsTitle = ChatColor.AQUA + "Tools";
    @Config
    private String defenceTitle = ChatColor.AQUA + "Defence";
    @Config
    private String customDefenceTitle = ChatColor.AQUA + "Custom Defence";
    
    private List<StoredInventory> inv = new List<>();
    
    private class StoredInventory
    {
        Player p;
        List<ISettingsComponent> comps;
    }

    public SettingsComponent(GameLogic logic, Material open, String title)
    {
        super(logic);
        mat = open;
        title = title;
    }
    public SettingsComponent(GameLogic logic){super(logic);}
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if(e.getItem() != null && e.getItem().getType() == mat)
        {
            settingmenu(p);
        }
    }
    
    @Override
    public int getSlot() {
        return 0;
    }
    
    @Override
    public ItemStack getItem() {
        return null;
    }
    
    @TriggerEvent(priority = 1, state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void onClick(InventoryClickEvent e)
    {
        if (e.getClickedInventory() == null){}
        String title = e.getClickedInventory().getTitle();
        Player p = (Player) e.getWhoClicked();
        if (title.equalsIgnoreCase(title)){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case WOOL:
                    defenceMenu(p);
                    return;
                case REDSTONE:
                    difficultyMenu(p);
                    return;
                case WOOD_PICKAXE:
                    toolsMenu(p);
                    return;
                case BARRIER:
                    p.closeInventory();
                    return;
            }
        }else if (title.equalsIgnoreCase(difficultyTitle)){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case COAL:
                    p.sendMessage(ChatColor.AQUA + "Difficulty have been set to " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case IRON_INGOT:
                    p.sendMessage(ChatColor.AQUA + "Difficulty have been set to " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case GOLD_INGOT:
                    p.sendMessage(ChatColor.AQUA + "Difficulty have been set to " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case DIAMOND:
                    p.sendMessage(ChatColor.AQUA + "Difficulty have been set to " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case BARRIER:
                    settingmenu(p);
                    return;
            }
        }else if (title.equalsIgnoreCase(toolsTitle)){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case WOOD_PICKAXE:
                    p.sendMessage(ChatColor.AQUA + "You have been given the toolset of " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case STONE_PICKAXE:
                    p.sendMessage(ChatColor.AQUA + "You have been given the toolset of " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case IRON_PICKAXE:
                    p.sendMessage(ChatColor.AQUA + "You have been given the toolset of " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case DIAMOND_PICKAXE:
                    p.sendMessage(ChatColor.AQUA + "You have been given the toolset of " + e.getCurrentItem().getType().name() + ChatColor.AQUA + "!");
                    return;
                case BARRIER:
                    settingmenu(p);
                    return;
            }
        }else if (title.equalsIgnoreCase(defenceTitle)){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case STAINED_GLASS:
                    if (e.getCurrentItem().getDurability() == DyeColor.RED.getData()){
                        p.sendMessage("You have choosen the typical Ranked Bedwars Defence! (Endstone and Clay)");
                    }else if (e.getCurrentItem().getDurability() == DyeColor.LIGHT_BLUE.getData()){
                        p.sendMessage("You have choosen the typical Ranked Bedwars Defence (Endstone And Wood)");
                    }
                    return;
                case REDSTONE_TORCH_ON:
                    customdefenceMenu(p);
                    return;
                case BARRIER:
                    settingmenu(p);
                    return;
            }
        }else if (title.equalsIgnoreCase(customDefenceTitle)){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case BARRIER:
                    defenceMenu(p);
                    return;
            }
        }
    }

    private void settingmenu(Player p){
        Inventory setting = Bukkit.createInventory(p,45,title);
        setting.setItem(4,new BetterItem(Material.BOOK).setDisplayName(ChatColor.GOLD + "Setting").setLore(ChatColor.YELLOW + "By changing settings here you can", ChatColor.YELLOW + "customised the gamemode to your likings").NaddEnchantment(Enchantment.DAMAGE_ALL, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS));
        setting.setItem(20,new BetterItem(Material.WOOL).setDisplayName(ChatColor.RED + "Defence Setting").setLore(ChatColor.RED + "Customise Your Bed Defence Here!", "",ChatColor.YELLOW + "Click Me!"));
        setting.setItem(22,new BetterItem(Material.REDSTONE).setDisplayName(ChatColor.RED + "Difficulty Setting").setLore(ChatColor.RED + "Change Your Gamemode Difficulty Here!", "",ChatColor.YELLOW + "Click Me!"));
        setting.setItem(24,new BetterItem(Material.WOOD_PICKAXE).setDisplayName(ChatColor.RED + "Tool Setting").setLore(ChatColor.RED + "Setup Your Tool Setting Here!", "",ChatColor.YELLOW + "Click Me!"));
        setting.setItem(40,new BetterItem(Material.BARRIER).setDisplayName(ChatColor.RED + "Close Menu").setLore(ChatColor.YELLOW + "Close This Menu Setting"));
        p.openInventory(setting);
    }

    private void difficultyMenu(Player p){
        Inventory difficuly = Bukkit.createInventory(p,27,difficultyTitle);
        difficuly.setItem(10,new BetterItem(Material.COAL).setDisplayName(ChatColor.GREEN + "Easy").setLore(ChatColor.YELLOW + "Click This To Pick " + ChatColor.RED + ChatColor.BOLD +  "EASY " + ChatColor.YELLOW + "Mode"));
        difficuly.setItem(12,new BetterItem(Material.IRON_INGOT).setDisplayName(ChatColor.BLUE + "Normal").setLore(ChatColor.YELLOW + "Click This To Pick " + ChatColor.RED + ChatColor.BOLD +  "NORMAL " + ChatColor.YELLOW + "Mode"));
        difficuly.setItem(14,new BetterItem(Material.GOLD_INGOT).setDisplayName(ChatColor.RED + "Hard").setLore(ChatColor.YELLOW + "Click This To Pick " + ChatColor.RED + ChatColor.BOLD +  "HARD " + ChatColor.YELLOW + "Mode"));
        difficuly.setItem(16,new BetterItem(Material.DIAMOND).setDisplayName(ChatColor.DARK_PURPLE + "Impossible").setLore(ChatColor.YELLOW + "Click This To Pick " + ChatColor.DARK_PURPLE + ChatColor.BOLD +  "IMPOSSIBLE " + ChatColor.YELLOW + "Mode"));
        difficuly.setItem(22,new BetterItem(Material.BARRIER).setDisplayName(ChatColor.RED + "Return To Main Setting").setLore(ChatColor.YELLOW + "Return To Main Setting"));
        p.openInventory(difficuly);
    }

    private void toolsMenu(Player p){
        Inventory tools = Bukkit.createInventory(p,27,toolsTitle);
        tools.setItem(10,new BetterItem(Material.WOOD_PICKAXE).setDisplayName(ChatColor.YELLOW + "Wooden Tools").setLore(ChatColor.YELLOW + "Click This To Pick " + ChatColor.GOLD + ChatColor.BOLD +  "WOODEN TOOLS " + ChatColor.YELLOW + "To Block In"));
        tools.setItem(12,new BetterItem(Material.STONE_PICKAXE).setDisplayName(ChatColor.YELLOW + "Stone Tools").setLore(ChatColor.YELLOW + "Click This To Pick" + ChatColor.GOLD + ChatColor.BOLD +  "STONE TOOLS " + ChatColor.YELLOW + "To Block In"));
        tools.setItem(14,new BetterItem(Material.IRON_PICKAXE).setDisplayName(ChatColor.YELLOW + "Iron Tools").setLore(ChatColor.YELLOW + "Click This To Pick " + ChatColor.GOLD + ChatColor.BOLD +  "IRON TOOLS" + ChatColor.YELLOW + "To Block In"));
        tools.setItem(16,new BetterItem(Material.DIAMOND_PICKAXE).setDisplayName(ChatColor.YELLOW + "Diamond Tools").setLore(ChatColor.YELLOW + "Click This To Pick " + ChatColor.GOLD + ChatColor.BOLD +  "DIAMOND TOOLS" + ChatColor.YELLOW + "To Block In"));
        tools.setItem(22,new BetterItem(Material.BARRIER).setDisplayName(ChatColor.RED + "Return To Main Setting").setLore(ChatColor.YELLOW + "Return To Main Setting"));
        p.openInventory(tools);
    }

    private void defenceMenu(Player p){
        Inventory defence = Bukkit.createInventory(p,27,defenceTitle);
        defence.setItem(11,new BetterItem(Material.STAINED_GLASS).NsetDurability((short) 14).setDisplayName(ChatColor.YELLOW + "Ranked Bedwars Defence (Clay)").setLore(ChatColor.YELLOW + "The typical Endstone Clay Bed Defence that is used in Ranked Bedwars"));
        defence.setItem(13,new BetterItem(Material.STAINED_GLASS).NsetDurability((short) 12).setDisplayName(ChatColor.YELLOW + "Ranked Bedwars Defence (Wood)").setLore(ChatColor.YELLOW + "The Endstone Wood Bed defence that is used in Ranked Bedwars"));
        defence.setItem(15,new BetterItem(Material.REDSTONE_TORCH_ON).setDisplayName(ChatColor.RED + "Custom Bed Defence"));
        defence.setItem(22,new BetterItem(Material.BARRIER).setDisplayName(ChatColor.RED + "Return To Main Setting").setLore(ChatColor.YELLOW + "Return To Main Setting"));
        p.openInventory(defence);
    }

    private void customdefenceMenu(Player p){
        Inventory customdefence = Bukkit.createInventory(p,54,customDefenceTitle);
        customdefence.setItem(53,new BetterItem(Material.BARRIER).setDisplayName(ChatColor.RED + "Return To Previous Menu").setLore(ChatColor.YELLOW + "Return To Previous Menu"));
        p.openInventory(customdefence);
    }

}

