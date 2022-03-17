package me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Settings;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsComponent extends GameComponent implements ISettingsComponent
{
    private  Settings settings = new Settings();
    private List<StoredInventory> inv = new ArrayList<>();
    private class Settings
    {
        private ItemStack mat;
        private String title = ChatColor.AQUA + "Settings";
    }


    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    private class StoredInventory
    {
        Player p;
        List<ISettingsComponent> comps;
    }

    public SettingsComponent(GameLogic logic, ItemStack open, String title)
    {
        super(logic);
        settings.mat = open;
        settings.title = title;
    }
    public SettingsComponent(GameLogic logic, String s)
{
    super(logic);
    settings = new Gson().fromJson(s, Settings.class);
}
    @Override
    public String getData() {return new Gson().toJson(settings);}

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof PlayerInteractEvent) onInteract((PlayerInteractEvent) event);
        if(event instanceof InventoryClickEvent) onClick((InventoryClickEvent) event);
        if(event instanceof InventoryOpenEvent) onInventoryOpen((InventoryOpenEvent) event);
    }

    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if(e.getItem() != null && e.getItem() == settings.mat)
        {
            Inventory setting = Bukkit.createInventory(p,45,settings.title);
            setting.setItem(4,new ItemBuilder(Material.BOOK).setName(ChatColor.GOLD + "Setting").setLore(ChatColor.YELLOW + "By changing settings here you can", ChatColor.YELLOW + "customised the gamemode to your likings").addEnchant(Enchantment.DAMAGE_ALL,1).itemFlags(ItemFlag.HIDE_ENCHANTS).toItemStack());
            setting.setItem(20,new ItemBuilder(Material.WOOL).setName(ChatColor.RED + "Defence Setting").setLore(ChatColor.RED + "Customise Your Bed Defence Here!", "",ChatColor.YELLOW + "Click Me!").toItemStack());
            setting.setItem(22,new ItemBuilder(Material.REDSTONE).setName(ChatColor.RED + "Difficulty Setting").setLore(ChatColor.RED + "Change Your Gamemode Difficulty Here!", "",ChatColor.YELLOW + "Click Me!").toItemStack());
            setting.setItem(24,new ItemBuilder(Material.WOOD_PICKAXE).setName(ChatColor.RED + "Tool Setting").setLore(ChatColor.RED + "Setup Your Tool Setting Here!", "",ChatColor.YELLOW + "Click Me!").toItemStack());
            p.openInventory(setting);
        }
    }

    public void onClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null){}
        String title = e.getClickedInventory().getTitle();
        Player p = (Player) e.getWhoClicked();
        if (title.equalsIgnoreCase(settings.title)){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case WOOL:
                    p.sendMessage("defence");
                    return;
                case REDSTONE:
                    p.sendMessage("difficulty");
                    return;
                case WOOD_PICKAXE:
                    p.sendMessage("tool setting");
                    return;
            }
        }
    }

    public void onInventoryOpen(InventoryOpenEvent e){
        if (e.getInventory().getTitle() != null && e.getInventory().getTitle() == ChatColor.AQUA + "Settings"){
            for(int i = 0; i < e.getInventory().getSize(); i++){
                if (e.getInventory().getItem(i).getType() == Material.AIR){
                    e.getInventory().setItem(i,new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDyeColor(DyeColor.RED).toItemStack());
                }
            }
        }
    }
}

