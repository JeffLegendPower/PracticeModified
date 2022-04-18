package me.trixxtraxx.Practice.Lobby;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class LobbyItem
{
    private ItemStack item;
    private int slot;
    
    public LobbyItem(final ItemStack item, final int slot) {
        this.item = item;
        this.slot = slot;
    }
    
    public LobbyItem(ConfigurationSection section)
    {
        Practice.log(4, "Getting Item: " + section.getCurrentPath());
        BetterItem item = new BetterItem(Material.valueOf(section.getString("Item.Material")));
        if(section.contains("Item.Name")) item.setDisplayName(section.getString("Item.Name"));
        if(section.contains("Item.Lore")) item.setLore(section.getStringList("Item.Lore"));
        this.item = item;
        this.slot = section.getInt("Item.Slot");
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public abstract void onClick(PlayerInteractEvent interact);
}
